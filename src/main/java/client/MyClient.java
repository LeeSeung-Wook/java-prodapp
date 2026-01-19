package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.RequestDTO;
import dto.ResponseDTO;
import server.Product;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 20000);

            BufferedReader keyBuf =
                    new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Gson gson = new Gson();

            printGuide();

            while (true) {
                System.out.print(">> ");
                String input = keyBuf.readLine();

                if (input.equals("exit")) {
                    bw.write("exit\n");
                    bw.flush();
                    break;
                }

                String[] arr = input.split(" ");
                String method = arr[0];

                RequestDTO req = null;

                // ===============================
                // 상품 상세 조회
                // get 1
                // ===============================
                if (method.equals("get")) {
                    int id = Integer.parseInt(arr[1]);

                    Map<String, Integer> qs = new HashMap<>();
                    qs.put("id", id);

                    req = new RequestDTO("get", qs, null);
                }

                // ===============================
                // 상품 전체 조회
                // getall
                // ===============================
                else if (method.equals("getall")) {
                    req = new RequestDTO("getall", null, null);
                }

                // ===============================
                // 상품 등록
                // post 바나나 1000 10
                // ===============================
                else if (method.equals("post")) {
                    String name = arr[1];
                    int price = Integer.parseInt(arr[2]);
                    int qty = Integer.parseInt(arr[3]);

                    Map<String, Object> body = new HashMap<>();
                    body.put("name", name);
                    body.put("price", price);
                    body.put("qty", qty);

                    req = new RequestDTO("post", null, body);
                }

                // ===============================
                // 상품 삭제
                // delete 3
                // ===============================
                else if (method.equals("delete")) {
                    int id = Integer.parseInt(arr[1]);

                    Map<String, Integer> qs = new HashMap<>();
                    qs.put("id", id);

                    req = new RequestDTO("delete", qs, null);
                }

                else {
                    System.out.println("알 수 없는 명령이다");
                    continue;
                }

                // 🔹 서버로 요청 전송
                bw.write(gson.toJson(req));
                bw.write("\n");
                bw.flush();

                // 🔹 서버 응답 수신
                String response = br.readLine();

                // ===============================
                // getall 응답 파싱 & 출력
                // ===============================
                if (method.equals("getall")) {
                    ResponseDTO<List<Product>> res =
                            gson.fromJson(
                                    response,
                                    TypeToken.getParameterized(ResponseDTO.class,
                                            TypeToken.getParameterized(List.class, Product.class).getType()
                                    ).getType()
                            );

                    List<Product> list = res.getBody();

                    if (list == null || list.isEmpty()) {
                        System.out.println("등록된 상품이 없다");
                    } else {
                        System.out.println("===== 상품 목록 =====");
                        for (Product p : list) {
                            System.out.println("상품번호: " + p.getId());
                            System.out.println("상품명  : " + p.getName());
                            System.out.println("가격    : " + p.getPrice() + "원");
                            System.out.println("수량    : " + p.getQty() + "개");
                            System.out.println("--------------------");
                        }
                    }
                }

                // ===============================
                // 나머지(get, post, delete)
                // ===============================
                else {
                    ResponseDTO<Product> res =
                            gson.fromJson(
                                    response,
                                    TypeToken.getParameterized(ResponseDTO.class, Product.class)
                                            .getType()
                            );

                    if (res.getBody() != null) {
                        Product p = res.getBody();
                        System.out.println("상품번호: " + p.getId());
                        System.out.println("상품명  : " + p.getName());
                        System.out.println("가격    : " + p.getPrice() + "원");
                        System.out.println("수량    : " + p.getQty() + "개");
                    } else {
                        System.out.println("응답: " + res.getMsg());
                    }
                }
            }

            keyBuf.close();
            bw.close();
            br.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printGuide() {
        System.out.println("===== 상품 관리 프로그램 =====");
        System.out.println("상품 상세 조회   : get 상품id");
        System.out.println("상품 전체 조회   : getall");
        System.out.println("상품 등록        : post 상품명 가격 수량");
        System.out.println("예시             : post 바나나 1000 10");
        System.out.println("상품 삭제        : delete 상품id");
        System.out.println("종료             : exit");
        System.out.println("================================");
    }
}
