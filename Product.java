package sw.pos;

public class Product {
	private String name, code, ep_date;//상품명, 상품 코드, 유통기한
	private int price;//가격
	public Product(String code, String name, String ep_date, String price) {
		super();
	    this.name = name;
	    this.code = code;
	    this.ep_date = ep_date;
	    this.price = Integer.parseInt(price);
	}

	public String getName() {
	    return name;
	}

	public String getCode() {
	    return code;
	}

	public String getEpdate() {
		return ep_date;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.code + "/" + this.name + "/" + this.ep_date + "/"+ this.price + "\n";
		return str;
	}
	
}
