package org.example.emailservice.template;

import org.springframework.stereotype.Component;

@Component
public class EventTemplates {
    public String passwordResetBody(String name,String token){
        return """
                    Merhaba %s,

                    Şifre sıfırlama talebiniz alındı.

                    Şifrenizi değiştirmek için aşağıdaki bağlantıyı kullanın:

                    http://localhost:5173/reset-password?token=%s


                    Bu bağlantı 30 dakika geçerlidir.

                    İyi günler.
                    """.formatted(name,token);
    }

    public String passwordChangeBody(String name,String token){

        return """
                    Merhaba %s,

                    Şifre Değiştirme talebiniz alındı.

                    Şifrenizi değiştirmek için aşağıdaki bağlantıyı kullanın:

                    http://localhost:5173/reset-password?token=%s


                    Bu bağlantı 30 dakika geçerlidir.

                    İyi günler.
                    """.formatted(name,token);
    }

    public String verificationBody(String name,String token){
        return """
            Hello %s,

            Please verify your email by clicking the link below:

            http://localhost:5173/verify?token=%s
            """.formatted(name, token);
    }
}
