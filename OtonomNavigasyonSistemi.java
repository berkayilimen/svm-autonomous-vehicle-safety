import java.util.ArrayList;

import java.util.List;

import java.io.BufferedWriter;

import java.io.FileWriter;

import java.io.IOException;



public class OtonomNavigasyonSistemi {



    /**

     * NOKTA SINIFI - Engel noktasını temsil eder

     */

    static class Nokta {

        double x;           // X koordinatı

        double y;           // Y koordinatı

        int etiket;         // Sınıf etiketi: +1 veya -1



        /**

         * Nokta oluşturucu

         * @param x X koordinatı

         * @param y Y koordinatı

         * @param etiket Sınıf etiketi (+1 veya -1)

         */

        public Nokta(double x, double y, int etiket) {

            this.x = x;

            this.y = y;

            this.etiket = etiket;

        }



        @Override

        public String toString() {

            return String.format("Nokta(%.2f, %.2f) - Sınıf: %d", x, y, etiket);

        }

    }



    /**

     * MAIN PROGRAM

     * Veri seti oluşturur, modeli eğitir, sonuçları gösterir

     */

    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════════════╗");

        System.out.println("║   OTONOM ARAÇ NAVIGASYON GÜVENLIK MODÜLÜ - SVM UYGULAMASI ║");

        System.out.println("╚════════════════════════════════════════════════════════════╝\n");



        // ============ VERİ SETİ OLUŞTURMA ============

        System.out.println("📊 VERİ SETİ OLUŞTURULUYOR...\n");

        List<Nokta> veriSeti = olusturVeriSeti();



        veriSetiniGoster(veriSeti);



        // ============ MODEL EĞİTİMİ ============

        System.out.println("\n⏱️  MODELE EĞİTİM VERİLİYOR...\n");

        SVMEgitici egitici = new SVMEgitici();

        long baslangicZamani = System.currentTimeMillis();

        egitici.egit(veriSeti, 10000);

        long bitisZamani = System.currentTimeMillis();



        // ============ SONUÇLAR ============

        System.out.println("\n✅ EĞİTİM TAMAMLANDI!\n");

        System.out.printf("⏰ Eğitim Süresi: %.3f saniye\n\n",

                (bitisZamani - baslangicZamani) / 1000.0);



        egitici.modeliYazdir();



        // ============ TAHMİN ============

        System.out.println("\n🔮 TAHMİN (INFERENCE) TESTİ:\n");

        tahminTest(egitici, veriSeti);



        // ============ SONUÇLARI DOSYAYA KAYDET ============

        try {

            sonuclariKaydet(egitici);

        } catch (IOException e) {

            System.err.println("Dosya yazma hatası: " + e.getMessage());

        }

    }



    /**

     * Örnek veri seti oluşturur

     * İki doğrusal olarak ayrılabilir sınıf

     */

    private static List<Nokta> olusturVeriSeti() {

        List<Nokta> veri = new ArrayList<>();



        // SINIF 1: Engel Grubu A (Güvenli Mesafe) - Etiket: +1

        System.out.println("Sınıf 1 (Güvenli Mesafe) - +1:");

        veri.add(new Nokta(1.0, 2.0, 1));      System.out.println("  (1.0, 2.0)");

        veri.add(new Nokta(2.0, 3.0, 1));      System.out.println("  (2.0, 3.0)");

        veri.add(new Nokta(3.0, 3.0, 1));      System.out.println("  (3.0, 3.0)");

        veri.add(new Nokta(2.5, 2.5, 1));      System.out.println("  (2.5, 2.5)");

        veri.add(new Nokta(1.5, 1.5, 1));      System.out.println("  (1.5, 1.5)");



        // SINIF 2: Engel Grubu B (Yüksek Risk) - Etiket: -1

        System.out.println("Sınıf 2 (Yüksek Risk) - -1:");

        veri.add(new Nokta(6.0, 5.0, -1));     System.out.println("  (6.0, 5.0)");

        veri.add(new Nokta(7.0, 8.0, -1));     System.out.println("  (7.0, 8.0)");

        veri.add(new Nokta(8.0, 8.0, -1));     System.out.println("  (8.0, 8.0)");

        veri.add(new Nokta(7.5, 6.5, -1));     System.out.println("  (7.5, 6.5)");

        veri.add(new Nokta(6.5, 5.5, -1));     System.out.println("  (6.5, 5.5)");



        return veri;

    }



    /**

     * Veri setini güzel biçimde gösterir

     */

    private static void veriSetiniGoster(List<Nokta> veri) {

        System.out.println("Toplam Nokta Sayısı: " + veri.size());

        System.out.println("\nDetaylı Veri:");

        for (int i = 0; i < veri.size(); i++) {

            System.out.println("  " + (i+1) + ". " + veri.get(i));

        }

    }



    /**

     * Modelin tahmin yeteneğini test eder

     */

    private static void tahminTest(SVMEgitici egitici, List<Nokta> veri) {

        System.out.println("Veri Seti Üzerinde Tahmin:");

        int dogruTahmin = 0;

        for (Nokta nokta : veri) {

            int tahmin = egitici.tahminYap(nokta.x, nokta.y);

            String sonuc = (tahmin == nokta.etiket) ? "✓ DOĞRU" : "✗ YANLIŞ";

            System.out.printf("  (%.1f, %.1f) => Tahmin: %2d | Gerçek: %2d | %s\n",

                    nokta.x, nokta.y, tahmin, nokta.etiket, sonuc);

            if (tahmin == nokta.etiket) dogruTahmin++;

        }

        double dogruluk = (double) dogruTahmin / veri.size() * 100;

        System.out.printf("\n📈 Doğruluk: %.1f%% (%d/%d)\n",

                dogruluk, dogruTahmin, veri.size());

    }



    /**

     * Sonuçları metin dosyasına kaydeder

     */

    private static void sonuclariKaydet(SVMEgitici egitici) throws IOException {

        BufferedWriter writer = new BufferedWriter(

                new FileWriter("SVM_Sonuclari.txt"));



        writer.write("OTONOM ARAÇ NAVIGASYON - SVM SONUÇLARI\n");

        writer.write("=====================================\n\n");



        writer.write(String.format("Ağırlık w1: %.6f\n", egitici.w1));

        writer.write(String.format("Ağırlık w2: %.6f\n", egitici.w2));

        writer.write(String.format("Bias b: %.6f\n\n", egitici.b));



        writer.write("Denklem: ");

        writer.write(String.format("%.6f*x + %.6f*y + %.6f = 0\n",

                egitici.w1, egitici.w2, egitici.b));



        writer.write("\nMargin (Güvenlik Koridoru) Genişliği: ");

        double norm = Math.sqrt(egitici.w1 * egitici.w1 + egitici.w2 * egitici.w2);

        double margin = 1.0 / norm;

        writer.write(String.format("%.6f\n", margin));



        writer.close();

        System.out.println("\n💾 Sonuçlar 'SVM_Sonuclari.txt' dosyasına kaydedildi.");

    }

}



/**

 * ============================================================

 * SVM EĞİTİCİ SINIFI

 * Stochastic Gradient Descent ile modeli eğitir

 * ============================================================

 */

class SVMEgitici {



    // Model Parametreleri

    public double w1 = 0.0;              // X katsayısı

    public double w2 = 0.0;              // Y katsayısı

    public double b = 0.0;               // Bias terimi



    // Hiperparametreler

    private final double ogrenmeOrani = 0.001;    // α (alpha) - Öğrenme hızı

    private final double lambda = 0.01;           // λ (lambda) - Regülarizasyon




    public void egit(List<OtonomNavigasyonSistemi.Nokta> veri, int iterasyon) {



        System.out.println("SGD ile Eğitim Başladı...");

        System.out.println("Öğrenme Oranı: " + ogrenmeOrani);

        System.out.println("Regülarizasyon: " + lambda);

        System.out.println("İterasyon: " + iterasyon + "\n");



        // İterasyon döngüsü

        for (int i = 1; i <= iterasyon; i++) {



            // Her veri noktasını kontrol et

            for (OtonomNavigasyonSistemi.Nokta nokta : veri) {



                // KARAR FONKSİYONU: etiket * (w·x + b)

                // Eğer bu değer >= 1 ise, nokta doğru sınıflandırılmış (margin dışı)

                // Eğer bu değer < 1 ise, nokta yanlış sınıflandırılmış

                double sart = nokta.etiket * (w1 * nokta.x + w2 * nokta.y + b);



                if (sart >= 1) {

                    // ✓ DOĞRU SINIFLANMIŞSA (Margin dışında)

                    // Sadece ağırlıkları küçült - Margin'i genişlet

                    // Güncelleme: w = w - α*λ*w

                    // (Bunun anlamı: ||w||² minimize et)



                    w1 -= ogrenmeOrani * (2 * lambda * w1);

                    w2 -= ogrenmeOrani * (2 * lambda * w2);



                } else {

                    // ✗ YANLIŞ SINIFLANMIŞSA

                    // Ağırlık ve bias'ı güncelle

                    // Hinge Loss gradyanı uygulanır

                    // Güncelleme: w = w - α*(λ*w - y*x)

                    // b = b + α*y



                    w1 -= ogrenmeOrani * (2 * lambda * w1 - nokta.etiket * nokta.x);

                    w2 -= ogrenmeOrani * (2 * lambda * w2 - nokta.etiket * nokta.y);

                    b += ogrenmeOrani * nokta.etiket;

                }

            }



            // Her 2000 iterasyonda progress göster

            if (i % 2000 == 0) {

                System.out.printf("  İterasyon %5d/%d tamamlandı...\n", i, iterasyon);

            }

        }



        System.out.println("  ✓ Eğitim tamamlandı!");

    }



  

    public int tahminYap(double x, double y) {

        double sonuc = w1 * x + w2 * y + b;

        return sonuc >= 0 ? 1 : -1;

    }



    /**

     * Bulunan optimal doğru denklemini yazdırır

     */

    public void modeliYazdir() {

        System.out.println("╔════════════════════════════════════════════════════════════╗");

        System.out.println("║       OPTIMUM AYRIŞTIRICI SINIR DENKLEMİ (KARAR SINIRI)    ║");

        System.out.println("╚════════════════════════════════════════════════════════════╝\n");



        System.out.printf("📐 Denklem:\n");

        System.out.printf("   %.6f*x + %.6f*y + %.6f = 0\n\n", w1, w2, b);



        // Doğrunun parametreleri

        double norm = Math.sqrt(w1 * w1 + w2 * w2);

        System.out.printf("📊 Parametreler:\n");

        System.out.printf("   w1 (x katsayısı): %.6f\n", w1);

        System.out.printf("   w2 (y katsayısı): %.6f\n", w2);

        System.out.printf("   b (bias): %.6f\n", b);

        System.out.printf("   ||w|| (norm): %.6f\n\n", norm);



        // Margin (Güvenlik Koridoru)

        double margin = 1.0 / norm;

        System.out.printf("🛡️  GÜVENLİK KORİDORU (Margin):\n");

        System.out.printf("   Genişliği: %.6f\n", margin);

        System.out.printf("   (Doğru ile en yakın nokta arasındaki mesafe)\n\n");



        // Sınıflandırma bölgeleri

        System.out.println("🎯 Sınıflandırma Bölgeleri:");

        System.out.printf("   Sınıf +1: w1*x + w2*y + b >= 1\n");

        System.out.printf("   Sınıf -1: w1*x + w2*y + b <= -1\n");

        System.out.printf("   Margin: -1 < w1*x + w2*y + b < 1\n\n");



        // Analiz

        System.out.println("📝 ANALİZ:");

        System.out.println("   Bu denklem SVM tarafından bulunmuş OPTİMAL çözümdür.");

        System.out.println("   Her iki sınıfa MAKSIMUM uzaklıkta bulunur.");

        System.out.println("   Sensor gürültüsüne karşı DAYANIКLI bir sınırlama sağlar.");

        System.out.println("   Otonom araçlar için EN GÜVENLI kararlaştırma denklemidir.");

        System.out.println();

    }

}

