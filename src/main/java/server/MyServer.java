package server;

import com.google.gson.Gson;
import dto.RequestDTO;
import dto.ResponseDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class MyServer {

    public static void main(String[] args) {
        try {
            // 1. 서버 소켓 생성
            ServerSocket ss = new ServerSocket(20000);
            System.out.println("서버 대기중.");

            Socket socket = ss.accept();
            System.out.println("클라이언트 연결됨.");

            // 2. 스트림 + 버퍼
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Gson gson = new Gson();
            ProductService productService = new ProductService();

            while (true) {
                // 3. 클라이언트 요청 수신
                String line = br.readLine();
                if (line == null) break;

                if (line.equals("exit")) {
                    System.out.println("서버 종료 요청이다");
                    break;
                }

                System.out.println("[server] " + line);

                try {
                    // 4. JSON 파싱
                    RequestDTO req = gson.fromJson(line, RequestDTO.class);
                    String method = req.getMethod();

                    // ===============================
                    // GET : 상품 상세
                    // ===============================
                    if ("get".equals(method)) {
                        int id = req.getQueryString().get("id");
                        Product product = productService.상품상세(id);

                        ResponseDTO<Product> res =
                                new ResponseDTO<>("ok", product);

                        bw.write(gson.toJson(res));
                        bw.write("\n");
                        bw.flush();
                    }

                    // ===============================
                    // GETALL : 상품 전체 조회
                    // ===============================
                    else if ("getall".equals(method)) {
                        List<Product> list = productService.상품목록();

                        ResponseDTO<List<Product>> res =
                                new ResponseDTO<>("ok", list);

                        bw.write(gson.toJson(res));
                        bw.write("\n");
                        bw.flush();
                    }

                    // ===============================
                    // POST : 상품 등록
                    // body = {name, price, qty}
                    // ===============================
                    else if ("post".equals(method)) {
                        Map<String, Object> body = req.getBody();

                        String name = (String) body.get("name");
                        int price = ((Number) body.get("price")).intValue();
                        int qty = ((Number) body.get("qty")).intValue();

                        productService.상품등록(name, price, qty);

                        ResponseDTO<Void> res =
                                new ResponseDTO<>("ok", null);

                        bw.write(gson.toJson(res));
                        bw.write("\n");
                        bw.flush();
                    }

                    // ===============================
                    // DELETE : 상품 삭제
                    // ===============================
                    else if ("delete".equals(method)) {
                        int id = req.getQueryString().get("id");
                        productService.상품삭제(id);

                        ResponseDTO<Void> res =
                                new ResponseDTO<>("ok", null);

                        bw.write(gson.toJson(res));
                        bw.write("\n");
                        bw.flush();
                    }

                    // ===============================
                    // 알 수 없는 요청
                    // ===============================
                    else {
                        ResponseDTO<Void> res =
                                new ResponseDTO<>("unsupported method", null);

                        bw.write(gson.toJson(res));
                        bw.write("\n");
                        bw.flush();
                    }

                } catch (Exception e) {
                    // 예외 응답
                    ResponseDTO<Void> res =
                            new ResponseDTO<>(e.getMessage(), null);

                    bw.write(gson.toJson(res));
                    bw.write("\n");
                    bw.flush();
                }
            }

            br.close();
            bw.close();
            socket.close();
            ss.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
