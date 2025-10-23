public class Arch {

    public static void main(String[] args) throws Throwable {
	Process process = Runtime.getRuntime().exec("arch");
	java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.InputStreamReader(process.getInputStream()));
	System.out.println(reader.readLine());
	reader.close();
    }

}
