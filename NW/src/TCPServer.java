import java.io.*;
import java.net.*;

public class TCPServer {

    static class Question {
        String question; // 문제
        String answer;   // 답

        public Question(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        Socket socket = null;

        try {
            listener = new ServerSocket(9999); // 서버 소켓 생성
            System.out.println("연결을 기다리고 있습니다.....");
            socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
            System.out.println("연결되었습니다.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Question 객체 배열 생성
            Question[] quiz = new Question[5];
            quiz[0] = new Question("Which of TCP and UDP does not require a connection setup process in advance? : ", "UDP");
            quiz[1] = new Question("What is the bit length of IPv6?(only number) : ", "128");
            quiz[2] = new Question("The routing table is stored in the router. (T/F) : ", "T");
            quiz[3] = new Question("HTTP uses TCP. (T/F) : ", "T");
            quiz[4] = new Question("UDP guarantees reliability. (T/F) : ", "F");

            int score = 0;

            for (int i = 0; i < quiz.length; i++) {
                // 문제 전달
                out.write(quiz[i].question + "\n");
                out.flush(); // 클라이언트가 문제를 바로 받을 수 있도록 flush

                // 클라이언트로부터 답 수신
                String inputMessage = in.readLine();
                System.out.println("클라이언트의 답: " + inputMessage);
                inputMessage = inputMessage.toUpperCase(); //모두 대문자로 바꿔주며 예외사항 처리
                // 답 확인
                if (inputMessage.equals(quiz[i].answer)) {
                    score++;
                    out.write("Correct!\n");
                } else {
                    out.write("Incorrect! Answer is " + quiz[i].answer + "\n"); //틀리면 정답도 출력
                }
                out.flush();
            }

            //점수 전송 1뮨제 당 1점
            out.write("Your score is " + score + "\n");
            out.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close(); // 통신용 소켓 닫기
                if (listener != null) listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
            }
        }
    }
}






