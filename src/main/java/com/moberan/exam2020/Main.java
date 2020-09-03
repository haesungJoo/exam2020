package com.moberan.exam2020;

import com.moberan.exam2020.lib.Task;
import com.moberan.exam2020.lib.TestLibrary;

public class Main {

	//해결 도출 과정
	//메인 스레드에 대한 존재를 간과함으로써 자원의 공유가 안된다는 점이 받아들이기 어려웠지만
	//적절한 구글링으로 해당하는 문제점의 메소드를 찾게 됨과 동시에 메인스레드의 존재를 알게되었고,
	//이를 문제에 적용하자 해결이 가능하였다.
	//(참조 사이트 : https://javaplant.tistory.com/29)
	//(아래 스레드의 액션에 대해 적어두었다.)
	//ps..만약 ms949로 이한 컴파일이 안된다면 run 버튼을 정확히 3번 눌러주면 된다.
	//    3번을 빠르게 누르는것이 아니라 결과값이 나오면 누르는 식으로 해주면 된다.
	//    첫번째 : 오류
	//    두번째 : Starting Gradle Daemon...(기다리면 실행이 되지만 급하다면 한번더 눌러주자)
	//    세번째 : 실행성공
	//    (다른 해결방법은 시도후 실패하였다.)

	public static void main(String[] args) {


		String result = tasks();
		System.out.println();
		System.out.println("Answer : "+result);

	}

	private static String tasks(){

		final TestLibrary lib = new TestLibrary();

		final String[] result = new String[1];

		final Object lock = new Object();

		lib.firstTask(new Task() {
			@Override
			public void taskCallback(String s) {
				result[0] = lib.secondTask(s);
				synchronized (lock){
					// notify를 통해 main 스레드 runnable로 변환
					lock.notify();
					// 현재 실행(락을 가지고 있는)되는 스레드 표시(이해를 돕기 위해 출력까지 함)
					System.out.println("---Before wait()---");
					System.out.println("Running Thread now : "+Thread.currentThread().getName());
				}
			}
		});

		synchronized (lock){
			try {
				// wait 메소드로 Thread-0의 lock을 반납
				// main 스레드를 실행시킴
				lock.wait();
				System.out.println();
				System.out.println("---After wait()---");
				System.out.println("Running Thread now : "+Thread.currentThread().getName());

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// FIXME always null.
		return result[0];
	}

}
