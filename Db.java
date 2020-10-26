package sw.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Db {
	private String last_date;//가장 최근의 날짜 저장 변수
	private HashMap<String, ArrayList<Product>> products = new HashMap<>();//상품 저장하는 리스트
	private HashMap<String, CategoryInfo> categorys;//큰 카테고리 저장하는 리스트
	private HashMap<String, NameInfo> names;//상품종류(상품이름) 저장하는 리스트
	private HashMap<Integer, Integer> cash;//현금보유량 저장하는 리스트
	private HashMap<String, ArrayList<Product>> payments = new HashMap<>();//결제 정보 저장 리스트
	
	private FileIO fileio;//파일 입출력 클래스
	
	public Db(String date) {
		fileio = new FileIO();
		//리스트들 파일내용 저장
		last_date = fileio.readLastDate();//최신 날짜.
		categorys = fileio.readCategory();
		names = fileio.readName();
		cash = fileio.readCash();
		payments = fileio.readPayment();
		for(int i=0; i<categorys.size(); i++) {//AA를 하나씩 증가시킴
			int num1 = (i/26)+65;
			int num2 = (i%26)+65;
			char ascii1 = (char)num1;
			char ascii2 = (char)num2;
			String file_name = "";
			file_name+=ascii1;
			file_name+=ascii2;
			products.put(file_name, fileio.readProduct((file_name+".txt")));
		}
	}
	//상품 추가 함수
	public void addProduct(Product product) {
		String file_name = product.getCode().substring(0, 2);
		if(!products.containsKey(file_name)) {
			ArrayList<Product> list = new ArrayList<Product>();
			list.add(product);
			products.put(file_name, list);
		}
		else {
			products.get(file_name).add(product);
		}
		
		ArrayList<String> contents = new ArrayList<>();
		for(int i=0; i<products.get(file_name).size(); i++) {
			String str = products.get(file_name).get(i).getCode();
			str+="/"+products.get(file_name).get(i).getName();
			str+="/"+products.get(file_name).get(i).getEpdate();
			str+="/"+products.get(file_name).get(i).getPrice()+"\n";
			contents.add(str);
		}
		fileio.writeFile((file_name+".txt"), contents);
	}
	//상품 제거 함수
	public void removeProduct(String code) {
		String file_name = code.substring(0, 2);
		for(int i=0; i<products.get(file_name).size(); i++) {
			if(products.get(file_name).get(i).getCode().equals(code)) {
				products.get(file_name).remove(i);
				break;
			}
		}
		ArrayList<String> contents = new ArrayList<>();
		for(int i=0; i<products.get(file_name).size(); i++) {
			String str = products.get(file_name).get(i).getCode();
			str+="/"+products.get(file_name).get(i).getName();
			str+="/"+products.get(file_name).get(i).getEpdate();
			str+="/"+products.get(file_name).get(i).getPrice()+"\n";
			contents.add(str);
		}
		fileio.writeFile((file_name+".txt"), contents);
	}
	//상품종류(상품이름) 변경 함수
	public void addNames(String name) {
		names.get(name).addLast_num();
		Set<String> set = names.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> contents = new ArrayList<>();
		String key;
		for(int i=0; i<names.size(); i++) {
			key = it.next();
			String str = key + "/" + names.get(key).getName_code() + "/" + names.get(key).getLast_num();
			str += "/" + names.get(key).getEpd_value()+"\n";
			contents.add(str);
		}
		fileio.writeFile("PName.txt", contents);
	}
	//상품종류(상품이름) 추가 함수
	public void addNames(String name, String code, int epd_value) {
		names.put(name, new NameInfo(code, 1, epd_value));
		String contents = name+"/"+code+"/1/"+epd_value;
		fileio.writeFile("PName.txt", contents);
	}
	//현금 잔량 변경 함수 
	public void setCash(int unit, int count, boolean isNegative) {
		int num = cash.get(unit);
		cash.remove(unit);
		if(isNegative)
			cash.put(unit, num-count);
		else
			cash.put(unit, num+count);
		ArrayList<String> contents = new ArrayList<>();
		contents.add("50000:"+cash.get(50000)+"\n");
		contents.add("10000:"+cash.get(10000)+"\n");
		contents.add("5000:"+cash.get(5000)+"\n");
		contents.add("1000:"+cash.get(1000)+"\n");
		contents.add("500:"+cash.get(500)+"\n");
		contents.add("100:"+cash.get(100)+"\n");
		contents.add("50:"+cash.get(50)+"\n");
		contents.add("10:"+cash.get(10)+"\n");
		fileio.writeFile("Cash.txt", contents);
	}
	//큰 카테고리 변경 함수
	public void addCategory(String cate) {
		categorys.get(cate).addLast_num();
		Set<String> set = categorys.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> contents = new ArrayList<>();
		String key;
		for(int i=0; i<categorys.size(); i++) {
			key = it.next();
			String str = key + "/" + categorys.get(key).getCategory_code() + "/" + categorys.get(key).getLast_num() + "\n";
			contents.add(str);
		}
		fileio.writeFile("Category.txt", contents);
	}
	//큰 카테고리 추가 함수
	public void addCategory(String cate, String code) {
		categorys.put(cate, new CategoryInfo(code, 1));
		String contents = cate+"/"+code+"/1";
		fileio.writeFile("Category.txt", contents);
	}
	//결제 기록 추가 함수
	public void addPayment(String code, ArrayList<Product> list) {
		payments.put(code, list);
		String str = code+"\n";
		for(int i=0; i<list.size(); i++) {
			str+= list.get(i).getCode();
			str+="/"+list.get(i).getName();
			str+="/"+list.get(i).getEpdate();
			str+="/"+list.get(i).getPrice()+"\n";
		}
		str+="@\n";
		fileio.writeFile("PaymentList.txt", str);
	}
	//결제한 상품 삭제 기능
	public void removePayment(String pay_code, String pro_code) {
		for(int i=0; i<payments.get(pay_code).size(); i++) {
			if(payments.get(pay_code).get(i).getCode().equals(pro_code)) {
				payments.get(pay_code).remove(i);
				if(payments.get(pay_code).isEmpty()) {
					payments.remove(pay_code);
				}
				break;
			}
		}
		
		Set<String> set = payments.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> contents = new ArrayList<>();
		String key;
		for(int i=0; i<payments.size(); i++) {
			key = it.next();
			String str = key+"\n";
			for(int j=0; j<payments.get(key).size(); j++) {
				str+=payments.get(key).get(j).getCode()+"/";
				str+=payments.get(key).get(j).getName()+"/";
				str+=payments.get(key).get(j).getEpdate()+"/";
				str+=payments.get(key).get(j).getPrice()+"\n";
			}
			str+="@\n";
			contents.add(str);
		}
		fileio.writeFile("PaymentList.txt", contents);
	}
	//현재 날짜보다 이전 날짜인지 판단.
	public String getLast_date() {
		return last_date;
	}
	
	public boolean isPossible(String date) {
		boolean isLast_date = false;
		//날짜는 무조건 20000101~20991231니까 index 2부터 index 7까지 보면 됨.
		
		if(isPossible(date, 2)) {
			isLast_date = true;
		}
		
		if(isLast_date) 
			return true;
		else {
			System.out.println("기존에 입력한 날짜보다 이후의 날짜만 입력 가능합니다.");
			System.out.println("다시 입력해주세요.");
			return false;
		}
		
	}
	
	public boolean isPossible(String date, int i) {
		if(i==8)
			return false;
		if(date.charAt(i)-'0' > last_date.charAt(i)-'0')
			return true;
		else if(date.charAt(i)-'0'== last_date.charAt(i)-'0')
			return isPossible(date, i+1);
		else
			return false;
	}
	
	//최근 날짜 변경 함수
	public String setLast_date(String last_date) {
			this.last_date = last_date;
			fileio.writeFile("Date.txt", last_date);
			return last_date.substring(0, 4)+"_"+last_date.substring(4, 6)+"_"+last_date.substring(6);
	}

	public HashMap<String, ArrayList<Product>> getProducts() {
		return products;
	}

	public HashMap<String, CategoryInfo> getCategorys() {
		return categorys;
	}

	public HashMap<String, NameInfo> getNames() {
		return names;
	}

	public HashMap<Integer, Integer> getCash() {
		return cash;
	}
	
}
