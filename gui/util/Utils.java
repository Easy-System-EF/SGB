package gui.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node)event.getSource()).getScene().getWindow();
	}
// transforma string do formulario em int
//		se houver branco ou letra, retorna null = insert	

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {
			return null;
		}	
	}

	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			return null;
		}	
	}
	
 	public static Double formatDecimalIn(String str) {
		try {
			NumberFormat nf_in = NumberFormat.getNumberInstance(Locale.ITALY);
	 		double val = nf_in.parse(str).doubleValue();
	 		return val;
		}
		catch (NumberFormatException e) {
			return null;
		} catch (ParseException e) {
 			e.printStackTrace();
		}
		return null;	
  	}
 	
// 	public static String formatDecimalOut(String str) {
//		try {
// 			NumberFormat nf_out = NumberFormat.getNumberInstance(Locale.ITALY);
// 			nf_out.setMaximumFractionDigits(2);
// 		  	String val = nf_out.format(str);
// 	 		return val;
//		}
//		catch (NumberFormatException e) {
//			return null;
//		}	
//
// 	}
  	
	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) { 
		  tableColumn.setCellFactory(column -> { 
		    TableCell<T, Date> cell = new TableCell<T, Date>() { 
		      private SimpleDateFormat sdf = new SimpleDateFormat(format); 
		 
		      @Override 
		      protected void updateItem(Date item, boolean empty) { 
		        super.updateItem(item, empty); 
		        if (empty) { 
		          setText(null); 
		        } else { 
		          setText(sdf.format(item)); 
		        } 
		      } 
		    }; 
		 
		    return cell; 
		  }); 
		} 
		 		 
 		public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) { 
		  tableColumn.setCellFactory(column -> { 
		    TableCell<T, Double> cell = new TableCell<T, Double>() { 
		 
		      @Override 
		      protected void updateItem(Double item, boolean empty) { 
		        super.updateItem(item, empty); 
		        if (empty) { 
		          setText(null); 
		        } else { 
  		          setText(String.format("%."+decimalPlaces+"f", item)); 
		        } 
		      } 
		    }; 
		 
		    return cell; 
		  }); 
		} 
		
		
		public static void formatDatePicker(DatePicker datePicker, String format) { 
			  datePicker.setConverter(new StringConverter<LocalDate>() { 
			     
			    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format); 
			 
			    { 
			      datePicker.setPromptText(format.toLowerCase()); 
			    } 
			 
			    @Override 
			    public String toString(LocalDate date) { 
			      if (date != null) { 
			        return dateFormatter.format(date); 
			      } else { 
			        return ""; 
			      } 
			    } 
			 
			    @Override 
			    public LocalDate fromString(String string) { 
			      if (string != null && !string.isEmpty()) { 
			        return LocalDate.parse(string, dateFormatter); 
			      } else { 
			        return null; 
			      } 
			    } 
			  }); 
			}

		public static void deleteArq(File file) {
System.out.println("utils 1 " + file);			
			while (true) {
				boolean vf = file.exists();
System.out.println("utils 2 " + vf);				
				if (vf == true) {
					file.delete();
					break;
				}
			}
		}	
		
		public static void imprime(String str) {
			File file = new File(str);
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.print(file);				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
	 	
 }
