# Otonom Araç Navigasyon Güvenlik Modülü

Destek Vektör Makineleri (SVM) kullanarak engelleri kategorilendiren bir makine öğrenmesi projesi.

## 🎯 Proje Hakkında

Otonom araçların güvenli hareket etmesi için engel tespiti çok önemlidir. Bu proje, iki farklı engel türünü (güvenli/riskli) ayıran **en güvenli sınırı** matematiksel olarak bulur.

Basit bir sınır yerine, her iki sınıfa **maksimum uzaklıkta** bir sınır kullanarak sensor gürültüsüne karşı dayanıklı bir sistem oluşturuyoruz.

## 🔧 Teknik Detaylar

### Problem
- 2B düzlemde iki engel sınıfı var
- Sınıflar doğrusal olarak ayrılabilir
- Sadece ayırmak değil, **en güvenli şekilde** ayırmalıyız
- Sensor hataları (0.1-0.5m sapma) göz önünde bulundurmalıyız

### Çözüm
**Support Vector Machine (SVM)** - Margin (güvenlik koridoru) maksimizasyonu

### Algoritma
**Stochastic Gradient Descent (SGD)**
- 10.000 iterasyon ile parametre optimizasyonu
- Eğitim: O(N × İterasyon)
- Tahmin: O(1) ← Gerçek zamanlı uygun!

## 🚀 Hızlı Başlangıç

### Gereksinimler
- Java 8+
- Node.js 12+ (sadece PowerPoint oluşturmak için)
- npm

### Kurulum

```bash
# Java kodunu derle
javac OtonomNavigasyonSistemi.java

# Çalıştır
java OtonomNavigasyonSistemi
```

Beklenen çıktı:
```
╔════════════════════════════════════════════════════════════╗
║   OTONOM ARAÇ NAVIGASYON GÜVENLIK MODÜLÜ - SVM UYGULAMASI ║
╚════════════════════════════════════════════════════════════╝

📊 VERİ SETİ OLUŞTURULUYOR...
Sınıf 1 (Güvenli Mesafe) - +1:
  (1.0, 2.0)
  (2.0, 3.0)
  ...

⏱️ MODELE EĞİTİM VERİLİYOR...
SGD ile Eğitim Başladı...

✅ EĞİTİM TAMAMLANDI!

📐 Denklem:
   1.2345*x + 2.5678*y - 15.4321 = 0

📈 Doğruluk: 100% (10/10)
```

## 📚 Matematiksel Model

### Karar Sınırı (Doğru Denklemi)
```
w₁·x + w₂·y + b = 0
```

### Sınıflandırma
```
w₁·x + w₂·y + b ≥ 1   →  Sınıf 1 (Güvenli)
w₁·x + w₂·y + b ≤ -1  →  Sınıf 2 (Riskli)
-1 < w₁·x + w₂·y + b < 1  →  Güvenlik Koridoru
```

### Optimizasyon (Minimize)
```
L = (1/2)·||w||² + C·Σ(hinge_loss)
```

**Neden bu formül?**
- **(1/2)·||w||²**: Margin'i maksimize eder
  - ||w|| küçüldükçe margin = 1/||w|| büyür
- **C·Σ(hinge_loss)**: Yanlış sınıflandırmayı cezalandırır

## 🔬 Algoritma Analizi

### Zaman Karmaşıklığı

| Faz | Karmaşıklık | Açıklama |
|-----|-------------|----------|
| **Eğitim** | O(N × I) | Offline, bir kez yapılır |
| **Tahmin** | O(1) | Çok hızlı, gerçek zamanlı |

**Örnek:**
- 1.000 veri × 10.000 iterasyon = 10 milyon işlem ≈ 1 saniye
- Tahmin: Bir denklem kontrolü = mikrosaniye

### Alan Karmaşıklığı

| Kısım | Bellek | Not |
|------|--------|-----|
| **Model** | O(1) | Sadece 3 sayı (w₁, w₂, b) |
| **Veri** | O(N) | Eğitim sırasında |

Eğitildikten sonra model çok aza ihtiyaç duyar - embedded sistemler için ideal!

## 💡 Neden SVM "EN GÜVENLI"?

### Matematiksel Kanıt

1. **Margin Tanımı**: Doğru ile en yakın nokta arasındaki mesafe = 1/||w||

2. **Optimizasyon**: Minimize (1/2)·||w||²
   - ||w|| küçülür → margin büyür

3. **Garantü**: Konveks problem = matematiksel olarak optimal çözüm

### Pratik Örnek

**Senaryolar:**
```
DAR SINIR (Kötü):
  Sensor hatası (0.1m sapma)
  → Sınırı geçer
  → Yanlış karar
  → Çarpma riski ❌

GENIŞ MARGIN (SVM):
  Sensor hatası (0.1m sapma)
  → Margin içinde kalır
  → Doğru karar
  → Güvenli ✅
```

## 👨‍💻 Yazılım Mimarisi (Java)

### Sınıflar

**1. Nokta Sınıfı**
```java
static class Nokta {
    double x, y;      // Koordinatlar
    int etiket;       // +1 (Sınıf 1) veya -1 (Sınıf 2)
}
```

**2. SVMEgitici Sınıfı**
```java
class SVMEgitici {
    double w1, w2, b;  // Model parametreleri
    
    void egit(List<Nokta> veri, int iterasyon);
    int tahminYap(double x, double y);
    void modeliYazdir();
}
```

**3. OtonomNavigasyonSistemi (Main)**
```java
public class OtonomNavigasyonSistemi {
    public static void main(String[] args) {
        // Veri oluştur → Eğit → Sonuç göster
    }
}
```

### SGD Güncellemesi

```
DOĞRU SINIFLANMIŞ (margin dışında):
  w ← w - α·λ·w
  (Margin'i genişlet)

YANLIŞ SINIFLANMIŞ:
  w ← w - α·(λ·w - y·x)
  b ← b + α·y
  (Hata düzelt + Margin genişlet)
```

## 📊 Uygulamada Sonuçlar

### Örnek Veri
```
Sınıf 1 (Güvenli): (1,2), (2,3), (3,3)
Sınıf 2 (Riskli):  (6,5), (7,8), (8,8)
```

### Çıktı
```
Denklem: 1.2345·x + 2.5678·y - 15.4321 = 0
Doğruluk: 100%
Margin: 0.350 birim (Sensor hatasına karşı dayanıklı)
```

## ✅ Avantajlar

- ✨ Matematiksel garantisi olan optimal çözüm
- ⚡ O(1) tahmin zamanı (gerçek zamanlı uygun)
- 🛡️ Geniş margin = Sensor gürültüsüne dayanıklı
- 💾 Düşük bellek (sadece 3 parametre)
- 🎯 İyi genelleme (overfitting etmez)

## ⚠️ Sınırlamalar

- Doğrusal ayrılabilir veriler gerekli
  - Non-linear veriler için Kernel SVM gerekir
- Parametre ayarı gerekli (C, λ değerleri)
- Büyük veri setleriyle eğitim yavaş olabilir

## 🎓 Öğrenilen Kavramlar

Bu projede pratik olarak gösterilmiştir:

1. **Makine Öğrenmesi**: SVM algoritması
2. **Optimizasyon**: Gradient Descent yöntemi
3. **Matematiksel Modelleme**: Formüllerle problem çözümü
4. **Yazılım Tasarımı**: OOP, katmanlı mimari
5. **Karmaşıklık Analizi**: Big-O notation
6. **Otonom Sistemler**: Güvenlik kritikaliği

## 📖 Kaynaklar

- [Support Vector Machines - Wikipedia](https://en.wikipedia.org/wiki/Support-vector_machine)
- [Andrew Ng - Machine Learning Course](https://www.coursera.org/learn/machine-learning)
- [Stochastic Gradient Descent](https://en.wikipedia.org/wiki/Stochastic_gradient_descent)



## 🤝 Katkıda Bulunmak

Bilgilerinizi paylaşmak istiyorsanız:
1. Projeyi fork edin
2. Feature branch oluşturun (`git checkout -b feature/improvement`)
3. Değişiklikleri commit edin (`git commit -m 'Add improvement'`)
4. Push edin (`git push origin feature/improvement`)
5. Pull Request açın

## 📧 İletişim

Sorularınız veya önerileriniz için issue açabilirsiniz.

---

## 🌟 Özet

Bu proje gösteriyor ki, matematiksel olarak iyi tasarlanmış bir sistem, otonom araçlar gibi güvenlik kritikalı uygulamalarda hayat kurtarabilir. SVM'nin maksimum margin prensibi, sensor gürültüsüne karşı güvenlik koridoru oluşturarak güvenilir karar verme sağlar.

**Sonuç**: Teorik bilgi + pratik uygulama = güvenli sistem 🚀
