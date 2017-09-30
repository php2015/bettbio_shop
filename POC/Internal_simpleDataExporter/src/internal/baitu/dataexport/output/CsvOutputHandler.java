package internal.baitu.dataexport.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import internal.baitu.dataexport.config.OutputConfig;
import internal.baitu.dataexport.intf.DataExportConstants;
import internal.baitu.dataexport.intf.OutputHandler;

public class CsvOutputHandler implements OutputHandler{

	protected String fileName;
	protected String filePostfix;
	protected int fileIndex;
	protected OutputStreamWriter output = null;
	protected int rowIndex;
	protected OutputConfig config;
	protected int maxRows;
	

	@Override
	public void init(String fileName, int splitSize, OutputConfig config) throws Exception {
		int pos = fileName.lastIndexOf('.');
		this.fileName = fileName.substring(0, pos);
		this.filePostfix = fileName.substring(pos+1);
		this.fileIndex = 0;
		this.rowIndex = 0;
		this.config = config;
		this.maxRows = splitSize;
		output = null;
	}

	@Override
	public void outputData(Map<String, Object> data) throws Exception {
		if (output == null || rowIndex >= maxRows){
			createOutputFile(data);
			writeHeader(data);
		}
		System.out.println(data);
		List<List<Object>> printArea = getDataPrintArea(data);
		for(List<Object> row: printArea){
			for(Object cell: row){
				writeCsvCell(cell==null?"":String.valueOf(cell));
			}
			writeLine();
		}
		
	}

	private List<List<Object>> getDataPrintArea(Map<String, Object> data) {
		List<List<Object>> rows = (List<List<Object>>) data.get(DataExportConstants.FIELD_VALUE_ROWS);
		List<List<Object>> printArea = new ArrayList<List<Object>>();
		for(List<Object> row : rows){
			List<List<Object>> rowPrintArea = getRowPrintArea(row);
			printArea.addAll(rowPrintArea);
		}
		return printArea;
	}

	private List<List<Object>> getRowPrintArea(List<Object> row) {
		List<Object> rowPrintArea = new ArrayList<Object>();
		maxRows = 1;
		for(Object value: row){
			if (value instanceof Map){
				List<List<Object>> cellPrintArea = getDataPrintArea((Map<String, Object>) value);
				maxRows = Math.max(maxRows, cellPrintArea.size());
				rowPrintArea.add(cellPrintArea);
			}else{
				rowPrintArea.add(value);
			}
		}
		
		List<List<Object>> result = new ArrayList<List<Object>>();
		for(int i=0;i<maxRows;i++){
			List<Object> rowResult = new ArrayList<Object>();
			for(Object cell: rowPrintArea){
				if (cell instanceof List){
					List<List<Object>> cellPrintArea = (List<List<Object>>) cell;
					if (i<cellPrintArea.size()){
						rowResult.addAll(cellPrintArea.get(i));
					}else{
						for(int j=0;j<cellPrintArea.get(0).size();j++){
							rowResult.add("");
						}
					}
				}else{
					if (i == 0){
						rowResult.add(cell);
					}else{
						rowResult.add("");
					}
				}
			}
			result.add(rowResult);
		}
		return result;
	}

	private void writeCsvCell(String value) throws Exception {
		String output;
		if (value == null) {
			output = "";
		} else {
			if (value.indexOf(',') >= 0 || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
				output = value.replace("\"", "\"\"");
				output = '"' + output + '"';
			} else {
				output = value;
			}
		}
		this.output.write(output + ",");
	}
	
	protected void writeHeader(Map<String, Object> data) throws Exception {
		List<String> headers = new ArrayList<String>();
		mergeHeader(headers, data);
		for(String name: headers){
			writeCsvCell(name);
		}
		writeLine();
	}

	private void writeLine() throws IOException {
		this.output.write("\r\n");
	}

	protected void mergeHeader(List<String> headers, Map<String, Object> data) {
		List<String> curHeaders = (List<String>) data.get(DataExportConstants.FIELD_NAME_LIST);
		List<List<Object>> curDatas = (List<List<Object>>) data.get(DataExportConstants.FIELD_VALUE_ROWS);
		List<Object> curRow = curDatas.get(0);
		for(int i=0;i<curHeaders.size();i++){
			Object value = curRow.get(i);
			if (value instanceof Map){
				mergeHeader(headers, (Map<String, Object>) value);
			}else{
				headers.add(curHeaders.get(i));
			}
		}
	}

	protected void createOutputFile(Map<String, Object> data) throws Exception {
		if (output != null){
			output.flush();
			output.close();
			output = null;
		}
		rowIndex = 0;
		File file = new File(config.getBaseFolder(), fileName+"."+(fileIndex++)+"."+filePostfix);
		if (file.exists()){
			file.delete();
		}
		file.getParentFile().mkdirs();
		file.createNewFile();
		output = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
	}

	@Override
	public void close() {
		try{
		output.flush();
		output.close();
		output = null;
		}catch (Exception e){
		}
	}

}
