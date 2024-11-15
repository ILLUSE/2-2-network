import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        // 기본값 설정
        String serverIP = "localhost";
        int port = 9999;

        // 서버 정보 파일 읽기
        File configFile = new File("server_info.dat");
        if (configFile.exists()) {
            try (BufferedReader configReader = new BufferedReader(new FileReader(configFile))) {
                serverIP = configReader.readLine();
                port = Integer.parseInt(configReader.readLine());
            } catch (IOException | NumberFormatException e) {
                System.out.println("서버 정보 파일 읽기 오류, 기본값을 사용합니다.");
                System.out.println("기본값 IP : localhost / port : 9999");
            }
        } else {
            System.out.println("서버 정보 파일이 없으므로 기본값을 사용합니다.");
            System.out.println("기본값 IP : localhost / port : 9999");
        }

        try {
            socket = new Socket(serverIP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                String question = in.readLine(); // 서버로부터 문제 수신
                if (question == null || question.startsWith("Your score")) { 
                    System.out.println(question); // 최종 점수 출력 후 종료
                    break;
                }

                System.out.print(question); // 문제 출력
                String answer = scanner.nextLine(); // 사용자가 답 입력
                out.write(answer + "\n"); // 답 전송
                out.flush();

                String feedback = in.readLine(); // 서버로부터 정답 여부 피드백 수신
                System.out.println(feedback); // 피드백 출력
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close();
                if (socket != null) socket.close(); // 클라이언트 소켓 닫기
            } catch (IOException e) {
                System.out.println("서버와 채팅 중 오류가 발생했습니다.");
            }
        }
    }
}







