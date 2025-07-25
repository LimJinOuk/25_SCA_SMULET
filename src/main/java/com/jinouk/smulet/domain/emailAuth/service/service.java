package com.jinouk.smulet.domain.emailAuth.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

import com.jinouk.smulet.domain.emailAuth.dto.dto_code;
import com.jinouk.smulet.domain.emailAuth.entity.entity;
import com.jinouk.smulet.domain.emailAuth.repository.repository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import com.jinouk.smulet.domain.emailAuth.serviceInter.serviceinter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class service implements serviceinter {


    private final JavaMailSender mailSender;

    @Autowired
    private repository repository;

    private String ePw;


    //Message 내용 생성
    @Override 
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException
    {
        System.out.println("메일 받을 사용자 : "+to);
        System.out.println("인증번호 : "+ePw);

        MimeMessage message = mailSender.createMimeMessage();


        message.addRecipients(Message.RecipientType.TO ,to);
        message.setSubject("SMULET 가입 인증 코드입니다."); // 이메일 제목

        String msgg = "";
        // msgg += "<img src=../resources/static/image/emailheader.jpg />"; // header image
        msgg += "<h1>안녕하세요</h1>";
        msgg += "<h1>상명대생을 위한 시간표 SMULET입니다.</h1>";
        msgg += "<br>";
        msgg += "<p>아래 인증코드를 회원가입 인증 코드 인증란에 입력해주세요</p>";
        msgg += "<br>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black'>";
        msgg += "<h3 style='color:blue'>회원가입 인증코드 입니다</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>" + ePw + "</strong></div><br/>" ; // 메일에 인증번호 ePw 넣기
        msgg += "</div>";
        // msgg += "<img src=../resources/static/image/emailfooter.jpg />"; // footer image
        message.setText(msgg, "utf-8", "html"); // 메일 내용, charset타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("jinouk038@gmail.com", "SMULET"));

        return message;
    }
    
    //인증번호 생성
    @Override
    public String createKey()
    {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String key = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return key;

    }

    //메일 발송
    @Override
    public String sendSimpleMessage(String to) throws Exception
    {
        ePw = createKey(); // 랜덤 인증코드 생성
        MimeMessage message = createMessage(to); // 메세지 생성
        try { // 예외처리
            mailSender.send(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        save_code(to, ePw); // DB조회 후 저장
        return ePw; // 메일로 사용자에게 보낸 인증코드를 서버로 반환! 인증코드 일치여부를 확인하기 위함
    }

    @Transactional
    public void save_code(String email, String code)
    {
        Optional<entity> ByEmail = repository.findByEmail(email);
        if (ByEmail.isPresent()) //이미 해당 메일에 대한 코드가 저장되어 있다면 삭제하고 다시 저장
        {
            repository.delete(ByEmail.get());
            entity entity = new entity();
            entity.setEmail(email);
            entity.setCode(code);
            repository.save(entity);
        }
        else //해당 메일이 없다면 저장
        {
            entity entity = new entity();
            entity.setEmail(email);
            entity.setCode(code);
            repository.save(entity);
        }
    }

    @Transactional
    public void check_code(dto_code dto_code) throws IllegalArgumentException
    {
        Optional<entity> byEmail = repository.findByEmail(dto_code.getEmail());
        if (byEmail.isPresent())
        {
            entity entity = byEmail.get();
            if (dto_code.getCode().equals(entity.getCode()))
            {
                repository.deleteByEmail(dto_code.getEmail());
            }
            else
            {
                throw new IllegalArgumentException("인증번호가 올바르지 않습니다.:");
            }
        }
        else //email을 찾을 수 없음
        {
            throw new IllegalArgumentException("해당 코드가 발급된 E-Mail을 찾을 수 없음");
        }
    }
}
