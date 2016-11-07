/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketdemoserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class SocketDemoServer {

    /**
     * @param args the command line arguments
     */
    static BufferedWriter bufferedWriter = null;
    static BufferedReader bufferedReader = null;
    static Socket socket = null;

    public static void main(String[] args) {

        try {
            // mở server socket
            ServerSocket serverSocket = new ServerSocket(2101);
            System.out.println("Đang chờ kết nối tới ");
            socket = serverSocket.accept();

            /*
            * 1.serverSocket.accept() mở cổng chờ client kết nối tới 
            * 2. dừng mọi sử lý của threat này
            * chờ client kết nối tới sẽ trả ra 1 socket tương ứng và tiếp tục công việc 
             */
            System.out.println("Đã Kết nối...");
            // làm việc với socket khi đã kết nối thành công 
            //============== dùng thread để xử lý việc gửi nhận đồng thời ===============
            new Thread(() -> {
                try {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    while (true) {
                        System.out.print("\nServer: ");
                        String mes = new Scanner(System.in).nextLine();
                        bufferedWriter.write("Server say : " + mes);   // đẩy toàn bộ dữ liệu cho socket để client nhân đc
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(SocketDemoServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
            /// ==================== Nhận ========================
            while (true) {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readline = bufferedReader.readLine();
                System.out.println("\nServer say: " + readline);
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketDemoServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketDemoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketDemoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
