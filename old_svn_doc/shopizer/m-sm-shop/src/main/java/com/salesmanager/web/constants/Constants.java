package com.salesmanager.web.constants;

public class Constants {
	
	public final static String SLASH = "/";
	public final static String EQUALS = "=";

    public final static String RESPONSE_STATUS = "STATUS";
    public final static String RESPONSE_SUCCESS = "SUCCESS";
//    public final static String DEFAULT_LANGUAGE = "en";
    public final static String DEFAULT_LANGUAGE = "zh";  //修改平台默认的语言
	public final static String LANGUAGE = "LANGUAGE";
	public final static String LANG = "lang";
	public final static String BREADCRUMB = "BREADCRUMB";

	public final static String HOME_MENU_KEY = "menu.home";
	public final static String HOME_URL = "/shop";
	public final static String ADMIN_URI = "/admin";
	public final static String SHOP_URI = "/shop";
	public final static String SHOP = "shop";
	public final static String REF = "ref";
	public final static String REF_C = "c:";
	public final static String REF_SPLITTER = ":";
	public final static String DEFAULT_STORE = "DEFAULT";
		
	public final static String FILE_NOT_FOUND = "File not found";
	


	public final static String DEFAULT_DOMAIN_NAME = "localhost:8080";

	public final static String ADMIN_STORE = "ADMIN_STORE";
	public final static String ADMIN_USER = "ADMIN_USER";
	public final static String MERCHANT_STORE = "MERCHANT_STORE";
	public final static String SHOPPING_CART = "SHOPPING_CART";
	public final static String CUSTOMER = "CUSTOMER";
	public final static String ORDER = "ORDER";
	public final static String ORDER_ID = "ORDER_ID";
	public final static String ORDER_ID_TOKEN = "ORDER_ID_TOKEN";
	public final static String SHIPPING_SUMMARY = "SHIPPING_SUMMARY";
	public final static String SHIPPING_OPTIONS = "SHIPPING_OPTIONS";
	public final static String ORDER_SUMMARY = "ORDER_SIMMARY";


	public final static String GROUP_ADMIN = "ADMIN";
	public final static String PERMISSION_AUTHENTICATED = "AUTH";
	public final static String PERMISSION_CUSTOMER_AUTHENTICATED = "AUTH_CUSTOMER";
	public final static String GROUP_SUPERADMIN = "SUPERADMIN";
	public final static String GROUP_CUSTOMER = "CUSTOMER";
	public final static String ANONYMOUS_CUSTOMER = "ANONYMOUS_CUSTOMER";


	public final static String CONTENT_IMAGE = "CONTENT";
	public final static String CONTENT_LANDING_PAGE = "LANDING_PAGE";
	public final static String CONTENT_CONTACT_US = "contact";

	public final static String STATIC_URI = "/static";
	public final static String FILES_URI = "/files";
	public final static String PRODUCT_URI= "/product";
	public final static String CATEGORY_URI = "/category";
	public final static String PRODUCT_ID_URI= "/productid";
	public final static String ORDER_DOWNLOAD_URI= "/order/download";

	public final static String URL_EXTENSION= ".html";
	public final static String REDIRECT_PREFIX ="redirect:";




	public final static String STORE_CONFIGURATION = "STORECONFIGURATION";

	public final static String HTTP_SCHEME= "http";

	public final static String MISSED_CACHE_KEY = "MISSED";
	public final static String CONTENT_CACHE_KEY = "CONTENT";
	public final static String CONTENT_PAGE_CACHE_KEY = "CONTENT_PAGE";
	public final static String CATEGORIES_CACHE_KEY = "CATALOG_CATEGORIES";
	public final static String PRODUCTS_GROUP_CACHE_KEY = "CATALOG_GROUP";
	public final static String SUBCATEGORIES_CACHE_KEY = "CATALOG_SUBCATEGORIES";
	public final static String RELATEDITEMS_CACHE_KEY = "CATALOG_RELATEDITEMS";
	public final static String MANUFACTURERS_BY_PRODUCTS_CACHE_KEY = "CATALOG_BRANDS_BY_PRODUCTS";
	public final static String CONFIG_CACHE_KEY = "CONFIG";

	public final static String REQUEST_CONTENT_OBJECTS = "CONTENT";
	public final static String REQUEST_CONTENT_PAGE_OBJECTS = "CONTENT_PAGE";
	public final static String REQUEST_TOP_CATEGORIES = "TOP_CATEGORIES";
	public final static String REQUEST_PAGE_INFORMATION = "PAGE_INFORMATION";
	public final static String REQUEST_SHOPPING_CART = "SHOPPING_CART";
	public final static String REQUEST_CONFIGS = "CONFIGS";
	public final static String REQUEST_Reagent = "TOP_Reagent";
	public final static String REQUEST_Instrument = "TOP_Instrument";
	public final static String REQUEST_Supplies = "TOP_Supplies";
	public final static String REQUEST_Others = "TOP_Others";
	

	public final static String KEY_FACEBOOK_PAGE_URL = "facebook_page_url";
	public final static String KEY_GOOGLE_ANALYTICS_URL = "google_analytics_url";
	public final static String KEY_GOOGLE_API_KEY = "google_api_key";
	public final static String KEY_TWITTER_HANDLE = "twitter_handle";


	public final static String CATEGORY_LINEAGE_DELIMITER = "/";
	public final static int MAX_REVIEW_RATING_SCORE = 5;
	public final static int MAX_ORDERS_PAGE = 5;
	public final static int MAX_ADDRESS_NUMBER = 5;
	public final static int MAX_INVOICES_NUMBER = 5;
	public final static String SUCCESS = "success";
	public final static String CANCEL = "cancel";
	
	public final static String START = "start";
	public final static String MAX = "max";
	
	public final static String CREDIT_CARD_YEARS_CACHE_KEY = "CREDIT_CARD_YEARS_CACHE_KEY";
	public final static String MONTHS_OF_YEAR_CACHE_KEY = "MONTHS_OF_YEAR_CACHE_KEY";
	
	public final static String INIT_TRANSACTION_KEY = "init_transaction";
	
	public static final String RECAPATCHA_PUBLIC_KEY="shopizer.recapatcha_public_key";
    public static final String RECAPATCHA_PRIVATE_KEY="shopizer.recapatcha_private_key";
    
    public final static String LINK_CODE = "LINK_CODE";
    
    //增加表格用的json数据的产品name串
    public final static String PRODUCT_NAME = "Name";
    public final static String PRODUCT_IMAGE = "Image";
    public final static String PRODUCT_PRICE = "Price";
    public final static String PRODUCT_OPRATE = "Operate";
    public final static String PRODUCT_DESCRITION = "Description";
    //基础数据MAP
    public final static String BASEDATATYPE_MAP = "BASEDATATYPE_MAP"; //注册返积分
  //#用户注册使用的积分
    public final static String REGIST_SCORE="REGIST_SCORE";
    //#下单积分比例
    public final static String RATIO_BUTILE="RATIO_BUTILE";
    //#用户推荐
    public final static String USER_SCORE="USER_SCORE";
   // #客户首次下单
    public final static String FIRST_SCORE="FIRST_SCORE";
    
    public final static String CATEGORY_SERVICE_CODE = "04"; //
    public final static String CATEGORY_INSTRUMENT_CODE = "03";
    public final static String CATEGORY_REAGENT_CODE = "01";
    public final static String CATEGORY_CONSUMERGOODS = "02";
}
