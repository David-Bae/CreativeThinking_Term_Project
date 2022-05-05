import java.util.*;

class Menu{
	Semester s;
	
	Menu(Semester s){
		this.s = s;
	}
	
	void printMenu() {
		int n;
		Scanner scanner = new Scanner(System.in);
				
		System.out.println("1. View Quiz by Subject.");
		System.out.println("2. Take a test.");
		System.out.println("0. EXIT PROGRAM.");
		
		n = scanner.nextInt();
		while(n != 0)
	}
}


class Semester{
	String semester;
	ArrayList<Subject> subjectList = new ArrayList<Subject>();
	
	Semester() {}
	
	Semester(String semester){
		this.semester = semester;
	}
	
	void addSubject(String subjectName) {
		subjectList.add(new Subject(subjectName));
	}

	void printSubject() {
		System.out.println("==== " + semester + " Subject List ====");
		System.out.println();
		
		for(int i = 0 ; i < subjectList.size() ; i++)
			System.out.println((i+1) + ". " + subjectList.get(i).subjectName);
		
		System.out.println();
		System.out.println("=============================");
	}
	
	void addQuiz(int index) {
		Scanner scanner = new Scanner(System.in);
		String question;
		String answer;
		
		System.out.print("Question : ");
		question = scanner.nextLine();
		System.out.print("Answer : ");
		answer = scanner.nextLine();
		
		subjectList.get(index).addQuiz(question, answer);
	}
}

class Subject{
	String subjectName;
	ArrayList<Quiz> quizList = new ArrayList<Quiz>();
	
	Subject() { this("Defualt"); }
	
	Subject(String subjectName){
		this.subjectName = subjectName;
	}
	
	int size() {
		return quizList.size();
	}
	
	void addQuiz(String question, String answer) {
		quizList.add(new Quiz(question, answer));
	}
}

class Quiz{
	String question;		//질문.
	String answer;			//답 (선택사항.)
	boolean lastTest;		//마지막 테스트, 맞았으면 true, 틀렸으면 false
	int wrongNum = 0;		//틀린 문제 개수.
	int testNum = 0;		//test 횟
	
	Quiz(String question, String answer){
		this.question = question;
		
		if(answer.equals(""))
			this.answer = "None.";
		else
			this.answer = answer;
	}
	
	void test() {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Question : " + question);
		scanner.nextLine();
		System.out.println("Answer : " + answer);
		System.out.println("Please enter 1 for right 0 for wrong");
		
		if(scanner.nextInt() == 0) {
			lastTest = false;
			wrongNum++;
		}
		
		testNum++;
		scanner.close();
	}
	
	double wrongAnswerRate() {
		return (double)wrongNum / testNum;
	}
}


public class ShuffleQuiz{
	public static void main(String[] args) {
		Semester s3 = new Semester("2022-1");
		Scanner scanner = new Scanner(System.in);
		int i = 0;
		
		
		s3.addSubject("Software Project");
		s3.addSubject("Data Structure");
		s3.addSubject("ACT");
		s3.printSubject();
		
		System.out.println("Enter the index");
		i = scanner.nextInt();
		s3.addQuiz(i);
		
		
		
	}
}