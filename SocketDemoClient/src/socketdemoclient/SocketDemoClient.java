/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketdemoclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class SocketDemoClient {

    /**
     * @param args the command line arguments
     */
    static BufferedWriter bufferedWriter = null;
    static BufferedReader bufferedReader = null;
    static Socket socket = null;

    public static void main(String[] args) {
        try {
            // để kết nối tới server
            System.out.println("Đang kết nối tới serverr");
            Socket socket = new Socket("localhost", 2101);  // tạo socket kết nối tới server
            System.out.println("kết nối server thành công ");

            // sau khi kết nối thì làm việc với socket thì như làm việc server
            //============== dùng thread để xử lý việc gửi nhận đồng thời ===============
            new Thread(() -> {
                try {

                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    while (true) {
                        System.out.println("\nMe: ");
                        String mes = new Scanner(System.in).nextLine();
                        bufferedWriter.write(mes);   // đẩy toàn bộ dữ liệu cho socket để client nhân đc
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(SocketDemoClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
            /// ==================== Nhận ========================
            while (true) {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readline = bufferedReader.readLine();
                System.out.println("\nMe say: " +readline);
            }

        } catch (IOException ex) {
            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketDemoClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
