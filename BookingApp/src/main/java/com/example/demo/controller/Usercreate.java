package com.example.demo.controller;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
public class Usercreate {

    @Autowired
    private UserRepository userRepository;

    // ユーザー新規追加画面表示
    @GetMapping("/usercreate")
    public String showCreateUserForm() {
        return "usercreate";
    }

    // ユーザー新規追加処理
    @PostMapping("/usercreate")
    public String createUser(User user) {
        userRepository.save(user);
        return "redirect:/usercreate";
    }
    
    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // ヘッダー行はスキップ

                User user = new User();
                user.setUsername(row.getCell(0).getStringCellValue());
                user.setUserid(row.getCell(1).getStringCellValue());
                user.setPassword(row.getCell(2).getStringCellValue());
                user.setUsertype((int) row.getCell(3).getNumericCellValue());

                userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "redirect:/home"; // 適宜変更
    }

}
