package com.salesmanager.core.business.merchant.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.constants.MeasureUnit;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table(name = "MERCHANT_STORE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class MerchantStore extends SalesManagerEntity<Long, MerchantStore> {
	private static final long serialVersionUID = 7671103335743647655L;
	
	
	public final static String DEFAULT_STORE = "DEFAULT";
	
	@Id
	@Column(name = "MERCHANT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "STORE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@NotEmpty
	@Column(name = "STORE_NAME", nullable=false, length=100)
	private String storename;
	
	@Pattern(regexp="^[a-zA-Z0-9_]*$")
	@Column(name = "STORE_CODE", length=100)
	private String code;
	
	@Column(name = "STORE_PHONE", length=50)
	private String storephone;
	
	@Column(name = "STORE_CONTACTS", length=50)
	private String storecontacts;

	@Column(name = "STORE_MOBILE", length=50)
	private String storemobile;
	
	@Column(name = "STORE_FAX", length=50)
	private String storefax;
	
	@Column(name = "STORE_QQ", length=100)
	private String qqNum;
	
	@Column(name = "isQQ")
	private Boolean useQQ =false;
	
	@Column(name = "STORE_ADDRESS")
	private String storeaddress;

	@Column(name = "STORE_CITY", length=100)
	private String storecity;
	
	@Column(name = "STORE_ENINTRODUCE", length=2000)
	private String enintroduce;
	
	@Column(name = "STORE_INTRODUCE", length=2000)
	private String introduce;
	
	@Column(name = "STORE_SEEDED")
	private Boolean seeded = false;

	//邮政编码可以为空
	//@NotEmpty
	@Column(name = "STORE_POSTAL_CODE", length=15)
	private String storepostalcode;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Country.class)
	@JoinColumn(name="COUNTRY_ID", nullable=true, updatable=true)
	private Country country;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Zone.class)
	@JoinColumn(name="ZONE_ID", nullable=true, updatable=true)
	private Zone zone;

	@Column(name = "STORE_STATE_PROV", length=100)
	private String storestateprovince;
	
	@Column(name = "WEIGHTUNITCODE", length=5)
	private String weightunitcode = MeasureUnit.LB.name();

	@Column(name = "SEIZEUNITCODE", length=5)
	private String seizeunitcode = MeasureUnit.IN.name();

	@Temporal(TemporalType.DATE)
	@Column(name = "IN_BUSINESS_SINCE")
	private Date inBusinessSince = new Date();
	
	@Transient
	private String dateBusinessSince;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
	@JoinColumn(name = "LANGUAGE_ID", nullable=false)
	private Language defaultLanguage;

	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "MERCHANT_LANGUAGE")
	private List<Language> languages = new ArrayList<Language>();
	
	@Column(name = "USE_CACHE")
	private boolean useCache = false;
	
	@Column(name="STORE_TEMPLATE", length=25)
	private String storeTemplate;
	
	@Column(name="INVOICE_TEMPLATE", length=25)
	private String invoiceTemplate;
	
	@Column(name="DOMAIN_NAME", length=80)
	private String domainName;
	
	@Column(name="CONTINUESHOPPINGURL", length=150)
	private String continueshoppingurl;
	
	@Column(name="STOREWEB", length=150)
	private String storeUrl;
	
	@Email
	@Column(name = "STORE_EMAIL", length=100, nullable=false)
	private String storeEmailAddress;
	
	@Email
	@Column(name = "SALES_EMAIL", length=100)
	private String salesEmailAddress;
	
	@Column(name="STORE_LOGO", length=100)
	private String storeLogo;
	
	@Column(name="STORE_BUSINESSLICENCE", length=100)
	private String businesslicence; //营业执照
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Currency.class)
	@JoinColumn(name = "CURRENCY_ID", nullable=false)
	private Currency currency;
	
	@Column(name = "CURRENCY_FORMAT_NATIONAL")
	private boolean currencyFormatNational;
	
	//销售
	@Column(name="KEFU",length=255)
	private String kefu;
	//销售
	@Column(name="XIAOSHOU",length=255)
	private String xiaoshou;
	
	public MerchantStore() {
	}
	
	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return this.id;
	}
	
	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getStorephone() {
		return storephone;
	}

	public void setStorephone(String storephone) {
		this.storephone = storephone;
	}

	public String getStoreaddress() {
		return storeaddress;
	}

	public void setStoreaddress(String storeaddress) {
		this.storeaddress = storeaddress;
	}

	public String getStorecity() {
		return storecity;
	}

	public void setStorecity(String storecity) {
		this.storecity = storecity;
	}

	public String getStorepostalcode() {
		return storepostalcode;
	}

	public void setStorepostalcode(String storepostalcode) {
		this.storepostalcode = storepostalcode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public String getStorestateprovince() {
		return storestateprovince;
	}

	public void setStorestateprovince(String storestateprovince) {
		this.storestateprovince = storestateprovince;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getWeightunitcode() {
		return weightunitcode;
	}

	public void setWeightunitcode(String weightunitcode) {
		this.weightunitcode = weightunitcode;
	}

	public String getSeizeunitcode() {
		return seizeunitcode;
	}

	public void setSeizeunitcode(String seizeunitcode) {
		this.seizeunitcode = seizeunitcode;
	}



	public Date getInBusinessSince() {
		return CloneUtils.clone(inBusinessSince);
	}

	public void setInBusinessSince(Date inBusinessSince) {
		this.inBusinessSince = CloneUtils.clone(inBusinessSince);
	}

	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}



	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
		
	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreTemplate() {
		return storeTemplate;
	}

	public void setStoreTemplate(String storeTemplate) {
		this.storeTemplate = storeTemplate;
	}

	public String getInvoiceTemplate() {
		return invoiceTemplate;
	}

	public void setInvoiceTemplate(String invoiceTemplate) {
		this.invoiceTemplate = invoiceTemplate;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getContinueshoppingurl() {
		return continueshoppingurl;
	}

	public void setContinueshoppingurl(String continueshoppingurl) {
		this.continueshoppingurl = continueshoppingurl;
	}

	public String getStoreEmailAddress() {
		return storeEmailAddress;
	}

	public void setStoreEmailAddress(String storeEmailAddress) {
		this.storeEmailAddress = storeEmailAddress;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDateBusinessSince(String dateBusinessSince) {
		this.dateBusinessSince = dateBusinessSince;
	}

	public String getDateBusinessSince() {
		return dateBusinessSince;
	}

	public void setCurrencyFormatNational(boolean currencyFormatNational) {
		this.currencyFormatNational = currencyFormatNational;
	}

	public boolean isCurrencyFormatNational() {
		return currencyFormatNational;
	}

	public String getStorecontacts() {
		return storecontacts;
	}

	public void setStorecontacts(String storecontacts) {
		this.storecontacts = storecontacts;
	}

	public String getStoremobile() {
		return storemobile;
	}

	public void setStoremobile(String storemobile) {
		this.storemobile = storemobile;
	}

	public String getEnintroduce() {
		return enintroduce;
	}

	public void setEnintroduce(String enintroduce) {
		this.enintroduce = enintroduce;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getStorefax() {
		return storefax;
	}

	public void setStorefax(String storefax) {
		this.storefax = storefax;
	}

	public String getBusinesslicence() {
		return businesslicence;
	}

	public void setBusinesslicence(String businesslicence) {
		this.businesslicence = businesslicence;
	}

	public Boolean getSeeded() {
		return seeded;
	}

	public void setSeeded(Boolean seeded) {
		this.seeded = seeded;
	}

	public String getQqNum() {
		if(!StringUtils.isBlank(this.qqNum)){
			return qqNum.trim();
		}
		return qqNum;
	}

	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}

	public Boolean getUseQQ() {
		return useQQ;
	}

	public void setUseQQ(Boolean useQQ) {
		this.useQQ = useQQ;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}

	public String getSalesEmailAddress() {
		return salesEmailAddress;
	}

	public void setSalesEmailAddress(String salesEmailAddress) {
		this.salesEmailAddress = salesEmailAddress;
	}

	public String getKefu() {
		return kefu;
	}

	public void setKefu(String kefu) {
		this.kefu = kefu;
	}

	public String getXiaoshou() {
		return xiaoshou;
	}

	public void setXiaoshou(String xiaoshou) {
		this.xiaoshou = xiaoshou;
	}
	

	
}
