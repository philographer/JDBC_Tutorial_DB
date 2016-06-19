import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		DB db = new DB();	// 데이터베이스 객체 생성
		int input;
		
		while(true){
			
			System.out.println("입력 하실 데이터베이스 쿼리의 타입을 설정하세요");
			System.out.println("1번 평점 평균이 3.5이상인 학생의 ID와 이름, 총 이수학점 수, 평점평균 출력");
			System.out.println("2번 입력값보다 평점 평균이 높은 학생의 ID와 이름, 총 이수학점 수, 평점평균 출력");
			System.out.println("3번 성적표 출력");
			
			try{
				
				input = sc.nextInt();
				System.out.println(input+"번 입력하셨습니다.");
			
			switch(input)
			{
			case 1:
				db.selectQueryOne();
				break;
			case 2:
				System.out.println("학점을 입력해 주세요.");
				sc.nextLine();
				String deptName = sc.nextLine();
				db.selectQueryTwo(deptName);
				break;
			case 3:
				System.out.println("학번을 입력해 주세요.");
				sc.nextLine();
				String paramQuery = sc.nextLine();
				db.selectQueryThree(paramQuery);
				break;
			default:
				System.out.println("잘못 입력하셨습니다");
				continue;
			}//switch
			
			}catch(Exception e){
				System.out.println("숫자를 입력하지 않으셨습니다. 다시 입력하세요.");
				sc.nextLine();
			}//catch
	}//while loop
}//main function
}//main class
