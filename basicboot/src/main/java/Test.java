import java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		
		/*
		 * JwtSessionManager manager = new JwtSessionManager(); for(int i=0;i<100;i++) {
		 * //System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new
		 * Date(System.currentTimeMillis() + manager.betweenMillis()))); try {
		 * Thread.sleep(20000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */
		
		
	}
	
	
	public Object TestMap() {
		
		boolean isCenterCut = false;
		try {
			
			try {
				
			}catch(Throwable e) {
				
			}finally {
				if(isCenterCut) {
					System.out.println("응답 센터컷 메시지");
					return new HashMap();
				}
			}
			
			System.out.println("응답 문자 메시지");
			return "응답전문";
		}catch(Throwable e){
			
		}finally {
			System.out.println("$$$$$$$ rlelase #################");
		}
		return isCenterCut;
	}
		
}
