import java.util.*;
import java.io.*;
import java.time.*;

class Menu{
	private static Scanner scanner = new Scanner(System.in);
	private static QuizList quizList;
	String fileName;
	
	Menu(String fileName){
		quizList = new QuizList(fileName);
		this.fileName = fileName;
	}
	
	boolean entryMenu() {
		int n = 0;
		System.out.println();
		System.out.println("1. 문제 보기.(오답률 높은 순)");
		System.out.println("2. 테스트 보기.");
		System.out.println("3. 퀴즈 추가하기.");
		System.out.println("4. 퀴즈 삭제하기.");
		System.out.println("0. 프로그램 종료.(자동 저장)");
		System.out.println();
		System.out.print("Choose the Menu : ");
		n = scanner.nextInt();
		scanner.nextLine();
		System.out.println();
		
		while(n < 0 || n > 4) {
			System.out.print("잘못된 입력입니다.(0, 1, 2, 3, 4 를 입력하세요.) : ");
			n = scanner.nextInt();
			scanner.nextLine();
		}
		
		switch(n) {
		case 1:
			lookAtQuiz();
			return true;
		case 2:
			quizList.examine();
			return true;
		case 3:
			addQuiz();
			return true;
		case 4:
			deleteQuiz();
			return true;
		default:
			save(fileName);					//파일에 퀴즈 저장.
			System.out.println("프로그램을 종료합니다.");
			return false;			
		}
	}
	
	static void lookAtQuiz() {						//구현이 필요해.
		quizList.sortByCorrectRate();
		
		for(int i =0 ; i<quizList.numOfQuiz() ; i++) {
			System.out.printf("%02d. ", i+1);
			System.out.println(quizList.get(i) + "\n");
		}
	}
	
	static void addQuiz() {
		String[] quiz = new String[3];
		System.out.print("문제를 입력하세요 : ");
		quiz[0] = scanner.nextLine();
		System.out.print("답을 입력하세요 : ");
		quiz[1] = scanner.nextLine();
		System.out.print("메모를 입력하세요 : ");
		quiz[2] = scanner.nextLine();
		
		quizList.add(quiz);
	}
	
	static void deleteQuiz() {
		for(int i=0 ; i<quizList.numOfQuiz() ; i++) {
			System.out.println((i+1) + ". " + quizList.get(i).getQuestion());
		}
		
		System.out.print("삭제할 질문의 번호를 입력하세요 : ");
		int n = scanner.nextInt();
		quizList.remove(n-1);
		System.out.println(n + "번 질문이 삭제되었습니다.");
	}
	
	static void save(String fileName) {
		File file = new File(fileName);
		BufferedWriter writer;
		
		try {
			if(!file.exists())
				file.createNewFile();
			
			FileWriter fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			
			for(int i=0 ; i<quizList.numOfQuiz() ; i++ ) {
				writer.write(quizList.get(i).toFile() + "\n");
			}
			
			writer.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}	
	}
}

class QuizList{
	private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
	
	QuizList(String filename){
		File file = new File(filename);
		Scanner fScanner = null;
		
		try {
			fScanner = new Scanner(file);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(fScanner.hasNext()) {
			String line = fScanner.nextLine();
			String[] quiz = line.split(";");
			quizList.add(new Quiz(quiz));
		}
		
		fScanner.close();		
	}
	
	int numOfQuiz() {
		return quizList.size();
	}
	
	void add(String[] quiz) {
		quizList.add(new Quiz(quiz[0], quiz[1], quiz[2]));
	}
	
	Quiz get(int index) {
		return quizList.get(index);
	}
	
	void remove(int index) {
		quizList.remove(index);
	}

	void examine() {
		ArrayList<Integer> wrong = new ArrayList<Integer>();
		ArrayList<Quiz> test = new ArrayList<Quiz>();
		
		for(int i=0 ; i<quizList.size() ; i++) {
			if(!quizList.get(i).getLastTest())
				wrong.add(i);
		}
		
		while(test.size() != 2) {
			int random = (int)(Math.random() * wrong.size());
			if(!test.contains(quizList.get(wrong.get(random))));
				test.add(quizList.get(wrong.get(random)));
		}
		
		int n = 0;
		sortByCorrectRate();
		while(test.size() != 5) {
			if(!test.contains(quizList.get(n)))
				test.add(quizList.get(n));
			n++;
		}
		
		n = 0;
		sortByTime();
		while(test.size() != 10) {
			if(!test.contains(quizList.get(n)))
				test.add(quizList.get(n));
			n++;
		}
		
		for(Quiz q : test) {
			q.test();
			System.out.println();
		}
		
	}
	
	void sortByCorrectRate() {
		int min;
		
		for(int i=0 ; i<quizList.size() ; i++) {
			min = i;
			
			for(int j=i+1 ; j<quizList.size(); j++) {
				if(quizList.get(j).getCorrectRate() < quizList.get(min).getCorrectRate())
					min = j;
			}
			
			if(i != min)
				quizList.add(i, quizList.remove(min));
		}
	}
	
	void sortByTime() {
		int last;
		
		for(int i=0 ; i<quizList.size() ; i++) {
			last = i;
			
			for(int j=i+1 ; j<quizList.size(); j++) {
				if(quizList.get(j).getTime().isBefore(quizList.get(last).getTime()))
					last = j;
			}
			
			if(i != last)
				quizList.add(i, quizList.remove(last));
		}
	}

}

class Quiz{
	static Scanner scanner = new Scanner(System.in);
	private String question;				//질문.
	private String answer;					//답.
	private boolean lastTest = false;		//마지막 테스트, 맞았으면 true, 틀렸으면 false
	private int correctNum = 0;				//맞은 문제 개수.
	private int testNum = 0;				//시도 횟수.
	private double correctRate = 0;
	private LocalDate time;					//마지막 시도 시간. (YYYY-MM-DD) 시간은 필요 없음.
	private String memo;					//참조할만한 것들 링크.
	
	Quiz(String question, String answer, String memo){		//새로운 quiz 추가할 때.
		this.question = question;
		this.answer = answer;
		this.memo = memo;
		this.time = LocalDate.of(2000, 1, 1);
	}
	
	Quiz(String[] contents){								//파일에서 quiz 불러올 때.
		String[] date = contents[6].split("-");
		
		question = contents[0];
		answer = contents[1];
		if(contents[2].equals("true"))
			lastTest = true;
		else
			lastTest = false;
		correctNum = Integer.parseInt(contents[3]);
		testNum = Integer.parseInt(contents[4]);
		correctRate = Double.parseDouble(contents[5]);
		time = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
		memo = contents[7];
	}
	
	void test() {
		int check;
		
		System.out.println("Question : " + question);
		System.out.print("(답을 확인하려면 Enter를 누르세요.)");
		scanner.nextLine();
		System.out.println("Answer : " + answer);
		System.out.print("맞았으면 1 틀렸으면 0을 입력하세요 : ");
		
		check = scanner.nextInt();
		scanner.nextLine();
		while(!(check==0 || check==1)) {
			System.out.print("잘못된 입력입니다. 다시 입력하세요(1 or 0) : ");
		}
		
		if(check == 0)
			lastTest = false;
		else {
			lastTest = true;
			correctNum++;
		}
		
		time = LocalDate.now();		
		testNum++;
		correctRate = (double)correctNum/testNum;
	}
	
	String printQuiz() {
		return "질문 : " + question;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public boolean getLastTest() {	
		return lastTest;
	}
	
	public double getCorrectRate() {
		return correctRate;
	}
	
	public LocalDate getTime() {
		return time;
	}
	
	public String toFile() {		//파일에 쓰기 위한 용도의 toString overriding.
		return question + ";" + answer + ";" + lastTest + ";" + correctNum + ";" + testNum + ";" + correctRate + ";" + time + ";" + memo;
	}

	public String toString() {
		return "Q : " + question + "\n    A : " + answer;
	}
}


public class ShuffleQuiz{
	public static void main(String[] args) {
		Menu program = new Menu("quizlist.txt");
		while(program.entryMenu()) { }
	}
}