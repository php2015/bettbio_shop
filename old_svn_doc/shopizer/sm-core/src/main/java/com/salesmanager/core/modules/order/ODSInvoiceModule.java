package com.salesmanager.core.modules.order;

import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.jopendocument.model.OpenDocument;
import org.jopendocument.renderer.ODTRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ProductUtils;

public class ODSInvoiceModule implements InvoiceModule {
	
	private final static String INVOICE_TEMPLATE = "templates/invoice/Invoice";
	private final static String INVOICE_TEMPLATE_EXTENSION = ".ods";
	private final static String TEMP_INVOICE_SUFFIX_NAME = "_invoice.ods";
	private final static int ADDRESS_ROW_START = 2;
	private final static int ADDRESS_ROW_END = 5;
	
	private final static int BILLTO_ROW_START = 8;
	private final static int BILLTO_ROW_END = 13;
	
	private final static int PRODUCT_ROW_START = 16;
	
	private static final Logger LOGGER = LoggerFactory.getLogger( ODSInvoiceModule.class );
	
	@Autowired
	private ZoneService zoneService;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private ProductPriceUtils priceUtil;
	

	@Override
	public ByteArrayOutputStream createInvoice(MerchantStore store, Order order, Language language) throws Exception {
		
		

			
			List<Zone> zones = zoneService.getZones(store.getCountry(), language);
			Map<String,Country> countries = countryService.getCountriesMap(language);
			
			//get default template
			String template = new StringBuilder().append(INVOICE_TEMPLATE).append("_").append(language.getCode().toLowerCase()).append(INVOICE_TEMPLATE_EXTENSION).toString();
			
			//try by language
			InputStream is = null;
			try {
				is = getClass().getClassLoader().getResourceAsStream(template);
			} catch (Exception e) {
				LOGGER.warn("Cannot open template " + template);
				throw new Exception("Cannot open " + template);
			}
			
			if(is==null) {
				try {
					is = getClass().getClassLoader().getResourceAsStream(new StringBuilder().append(INVOICE_TEMPLATE).append(INVOICE_TEMPLATE_EXTENSION).toString());
				} catch (Exception e) {
					LOGGER.warn("Cannot open template " + template);
					throw new Exception("Cannot open " + new StringBuilder().append(INVOICE_TEMPLATE).append(INVOICE_TEMPLATE_EXTENSION).toString());
				}
			}
			
			if(is==null) {
				LOGGER.warn("Cannot open template " + template);
				throw new Exception("Cannot open " + new StringBuilder().append(INVOICE_TEMPLATE).append(INVOICE_TEMPLATE_EXTENSION).toString());
			}
			
			File file = new File(order.getId() + "_working");
			OutputStream os = new FileOutputStream(file);
			IOUtils.copy(is, os);
			os.close();
			//File file = new File(resource.toURI().toURL());
		
			Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
			
			
			//Store name 
			sheet.setValueAt(store.getStorename(), 0, 0);
			
			
			
			//Address
			//count store address cell
			int storeAddressCell = ADDRESS_ROW_START;
			
			Map<String,Zone> zns = zoneService.getZones(language);

			
			//3
			StringBuilder storeAddress = null;
			if(!StringUtils.isBlank(store.getStoreaddress())) {
				storeAddress = new StringBuilder();
				storeAddress.append(store.getStoreaddress());
			}
			if(!StringUtils.isBlank(store.getStorecity())) {
				if(storeAddress==null) {
					storeAddress = new StringBuilder();
				} else {
					storeAddress.append(", ");
				}
				storeAddress.append(store.getStorecity());
			}
			if(storeAddress!=null) {
				sheet.setValueAt(storeAddress.toString(), 0, storeAddressCell);
				storeAddressCell ++;
			}
			
			//4
			StringBuilder storeProvince = null;
			if(store.getZone()!=null) {
				storeProvince = new StringBuilder();
				
				for(Zone z : zones) {
					if(z.getCode().equals(store.getZone().getCode())) {
						storeProvince.append(z.getName());
						break;
					}
				}
				
			} else {
				if(!StringUtils.isBlank(store.getStorestateprovince())) {
					storeProvince = new StringBuilder();
					storeProvince.append(store.getStorestateprovince());
				}
			}
			if(store.getCountry()!=null) {
				if(storeProvince==null) {
					storeProvince = new StringBuilder();
				} else {
					storeProvince.append(", ");
				}
				
				Country c = countries.get(store.getCountry().getIsoCode());
				if(c!=null) {
					storeProvince.append(c.getName());
				} else {
					storeProvince.append(store.getCountry().getIsoCode());
				}
				
			}
			if(storeProvince!=null) {
				sheet.setValueAt(storeProvince.toString(), 0, storeAddressCell);
				storeAddressCell ++;
			}
			
			//5
			if(!StringUtils.isBlank(store.getStorepostalcode())) {
				sheet.setValueAt(store.getStorepostalcode(), 0, storeAddressCell);
				storeAddressCell ++;
			}
			
			//6
			if(!StringUtils.isBlank(store.getStorephone())) {
				sheet.setValueAt(store.getStorephone(), 0, storeAddressCell);
			}
			
			//delete address blank lines
			for(int i = storeAddressCell; i<ADDRESS_ROW_END; i++) {
				sheet.setValueAt("", 0, i);
			}

			//invoice date
			SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
			sheet.setValueAt(format.format(order.getDatePurchased()), 3, 2);
			
			//invoice number
			sheet.setValueAt(order.getId(), 3, 3);
			
			//bill to
			//count bill to address cell
			int billToCell = BILLTO_ROW_START;
			if(!StringUtils.isBlank(order.getBillingUserName())) {
				StringBuilder nm = new StringBuilder();
				nm.append(order.getBillingUserName());
				sheet.setValueAt(nm.toString(), 0, billToCell);
				billToCell ++;
			}
			
			//9
			if(!StringUtils.isBlank(order.getBillingInvoice().getCompany())) {
				sheet.setValueAt(order.getBillingInvoice().getCompany(), 0, billToCell);
				billToCell ++;
			}
			
			//10
			StringBuilder billToAddress = null;
			if(!StringUtils.isBlank(order.getBillingAddress())) {
				billToAddress = new StringBuilder();
				billToAddress.append(order.getBillingAddress());
			}
			if(!StringUtils.isBlank(order.getBillingCity())) {
				if(billToAddress==null) {
					billToAddress = new StringBuilder();
				} else {
					billToAddress.append(", ");
				}
				billToAddress.append(order.getBillingCity());
			}
			if(billToAddress!=null) {
				sheet.setValueAt(billToAddress.toString(), 0, billToCell);
				billToCell ++;
			}
			
			//11
			StringBuilder billToProvince = null;
			if(order.getBillingZone()!=null) {
				billToProvince = new StringBuilder();
				
				Zone billingZone = zns.get(order.getBillingZone().getCode());
				if(billingZone!=null) {
						billToProvince.append(billingZone.getName());
				}
				
			} 
			if(order.getBillingCountry()!=null) {
				if(billToProvince==null) {
					billToProvince = new StringBuilder();
				} else {
					billToProvince.append(", ");
				}
				Country c = countries.get(order.getBillingCountry().getIsoCode());
				if(c!=null) {
					billToProvince.append(c.getName());
				} else {
					billToProvince.append(order.getBillingCountry().getIsoCode());
				}
				
			}
			if(billToProvince!=null) {
				sheet.setValueAt(billToProvince.toString(), 0, billToCell);
				billToCell ++;
			}
			
			//12
			if(!StringUtils.isBlank(order.getBillingPostalCode())) {
				billToCell ++;
				sheet.setValueAt(order.getBillingPostalCode(), 0, billToCell);
				billToCell ++;
			}
			
			//13
			if(!StringUtils.isBlank(order.getBillingTelephone())) {
				sheet.setValueAt(order.getBillingTelephone(), 0, billToCell);
			}
			
			//delete address blank lines
			for(int i = billToCell; i<BILLTO_ROW_END; i++) {
				sheet.setValueAt("", 0, i);
			}
			
			//products
			/**
			Set<OrderProduct> orderProducts = order.getOrderProducts();
			int productCell = PRODUCT_ROW_START;
			for(OrderProduct orderProduct : orderProducts) {
				

				String orderProductName = ProductUtils.buildOrderProductDisplayName(orderProduct);
				
				sheet.setValueAt(orderProductName.toString(), 0, productCell);
				
				int quantity = orderProduct.getProductQuantity();
				sheet.setValueAt(quantity, 1, productCell);
				String amount = priceUtil.getStoreFormatedAmountWithCurrency(store, orderProduct.getOneTimeCharge());
				sheet.setValueAt(amount, 2, productCell);
				String t = priceUtil.getStoreFormatedAmountWithCurrency(store, priceUtil.getOrderProductTotalPrice(store, orderProduct));
				sheet.setValueAt(t, 3, productCell);

				productCell++;
				
			}
			
			//print totals
			productCell++;
			Set<OrderTotal> totals = order.getOrderTotal();
			for(OrderTotal orderTotal : totals) {
				
				String totalName = orderTotal.getText();
				if(totalName.contains(".")) {
					totalName = orderTotal.getTitle();
				}
				String totalValue = priceUtil.getStoreFormatedAmountWithCurrency(store,orderTotal.getValue());
				sheet.setValueAt(totalName, 2, productCell);
				sheet.setValueAt(totalValue, 3, productCell);
				productCell++;
			}
			*/
			//sheet.getCellAt(0, 0).setImage(arg0)
			//sheet.getCellAt(0, 0).setStyleName(arg0)
			//sheet.getCellAt(0, 0).getStyle().
			
			
			//generate invoice file
			StringBuilder tempInvoiceName = new StringBuilder();
			tempInvoiceName.append(order.getId()).append(TEMP_INVOICE_SUFFIX_NAME);
			File outputFile = new File(tempInvoiceName.toString());
			OOUtils.open(sheet.getSpreadSheet().saveAs(outputFile));
			
			
			
			final OpenDocument doc = new OpenDocument();
			doc.loadFrom(tempInvoiceName.toString());

			 // Open the PDF document
			 Document document = new Document(PageSize.A4);
			 
			 
			 //File outFile = new File("invoice.pdf");

			 PdfDocument pdf = new PdfDocument();

			 document.addDocListener(pdf);

			 //FileOutputStream fileOutputStream = new FileOutputStream(outFile);
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			 
			 PdfWriter writer = PdfWriter.getInstance(pdf, outputStream);
			 pdf.addWriter(writer);

			 document.open();

			 // Create a template and a Graphics2D object 
			 Rectangle pageSize = document.getPageSize();
			 int w = (int) (pageSize.getWidth() * 0.9);
			 int h = (int) (pageSize.getHeight() * 0.95);
			 PdfContentByte cb = writer.getDirectContent();
			 PdfTemplate tp = cb.createTemplate(w, h);

			 Graphics2D g2 = tp.createPrinterGraphics(w, h, null);
			 // If you want to prevent copy/paste, you can use
			 // g2 = tp.createGraphicsShapes(w, h, true, 0.9f);
			            
			 tp.setWidth(w);
			 tp.setHeight(h);

			 // Configure the renderer
			 ODTRenderer renderer = new ODTRenderer(doc);
			 renderer.setIgnoreMargins(true);
			 renderer.setPaintMaxResolution(true);
			            
			 // Scale the renderer to fit width
			 renderer.setResizeFactor(renderer.getPrintWidth() / w);
			 // Render
			 renderer.paintComponent(g2);
			 g2.dispose();

			 // Add our spreadsheet in the middle of the page
			 float offsetX = (pageSize.getWidth() - w) / 2;
			 float offsetY = (pageSize.getHeight() - h) / 2;
			 cb.addTemplate(tp, offsetX, offsetY);
			 // Close the PDF document
			 document.close();
			 outputFile.delete();//remove temp file
			 file.delete();//remove spreadsheet file
			 is.close();
			 return outputStream;
		
		

	}

}
