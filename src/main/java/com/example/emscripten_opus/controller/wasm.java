package com.example.emscripten_opus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

@Controller
public class wasm {
    @RequestMapping("getWasm")
    public void getWasm(HttpServletRequest request, HttpServletResponse response) {
        URL resource = WebApplicationContext.class.getClassLoader().getResource("static/decoder.wasm");
        File file = new File(resource.getPath());
        response.setHeader("Content-Type","application/wasm");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=fileInputStream.read(bytes))>0){
                response.getOutputStream().write(bytes,0,len);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @RequestMapping("getHelloWasm")
    public void getHelloWasm(HttpServletRequest request, HttpServletResponse response) {
        URL resource = WebApplicationContext.class.getClassLoader().getResource("static/helloworld.wasm");
        File file = new File(resource.getPath());
        response.setHeader("Content-Type","application/wasm");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=fileInputStream.read(bytes))>0){
                response.getOutputStream().write(bytes,0,len);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
