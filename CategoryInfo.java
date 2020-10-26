package sw.pos;

public class CategoryInfo {
	private int last_num;//동일 카테고리 내에 생성된 상품종류 개수
	private String category_code;//큰 카테고리 코드 2자리
	
	public CategoryInfo(String category_code, int last_num) {
		this.category_code = category_code;
		this.last_num = last_num;
	}

	public int getLast_num() {
		return last_num;
	}

	public void addLast_num() {
		this.last_num++;
	}

	public String getCategory_code() {
		return category_code;
	}
	
}
