package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {

	Scanner scan;
	File file = new File("E:/furhat_skills/morningcall.csv");
	ArrayList<String> calls = new ArrayList<String>();

	// Read the data file
	public void readcall() throws FileNotFoundException {

		scan = new Scanner(file);
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] temp = line.split(",");
			calls.add(temp[0]);
			calls.add(temp[1]);
			calls.add(temp[2]);
		}
	}

	public ArrayList<String> getCalls() {
		return calls;
	}

	// Write the new call into the data file
	public void writecall(String date, String time, String room) throws IOException{
		String file = "E:/furhat_skills/morningcall.csv";

			BufferedWriter output=new BufferedWriter(new FileWriter(file,true));
			output.write(date+","+time+","+room);
			output.newLine();
			output.flush(); // if do not use flush, the output file can not be written completely
			output.close();
	}

	// Modify the data file when calls are edit
	public void modifycsv(String oldcall, String newcall) throws IOException {

		FileReader fr = new FileReader(file);
		BufferedReader bfr = new BufferedReader(fr);

		// 内存流, 作为临时流
		CharArrayWriter tempStream = new CharArrayWriter();

		// 替换
		String line = null;

		while ( (line = bfr.readLine()) != null) {
			// 替换每行中, 符合条件的字符串
			line = line.replace(oldcall, newcall);
			// 将该行写入内存
			tempStream.write(line);
			// 添加换行符
			tempStream.append(System.getProperty("line.separator"));
		}

		// 关闭 输入流
		bfr.close();

		// 将内存中的流 写入 文件
		FileWriter out = new FileWriter(file);
		tempStream.writeTo(out);
		out.close();


	}

	public void delete() throws IOException {

		FileReader fr = new FileReader(file);
		ChronoLocalDate datenow = LocalDate.now();
		BufferedReader bfr = new BufferedReader(fr);
		String str = null;
		ArrayList list = new ArrayList();
		while( (str=bfr.readLine()) != null ) {
			String[] temp =  str.split(",");
			LocalDate dateformat = LocalDate.parse(temp[0]);
			if(dateformat.isBefore(datenow)) {
				continue;
			}
		     list.add(str);
		    }

		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		for( int i=0;i<list.size();i++ ){
			bw.write(list.get(i).toString());
		    bw.newLine();
		    }
		 bw.flush();
		 bw.close();

	}



}
