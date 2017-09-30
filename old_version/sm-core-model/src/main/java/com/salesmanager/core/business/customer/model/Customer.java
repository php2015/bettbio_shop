package com.salesmanager.core.business.customer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.business.catalog.product.model.image.CustomerImage;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.customer.model.attribute.CustomerAttribute;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CUSTOMER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Customer extends SalesManagerEntity<Long, Customer> implements Auditable{
	private static final long serialVersionUID = -6966934116557219193L;
	
	@Id
	@Column(name = "CUSTOMER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "customer")
	private Set<CustomerAttribute> attributes = new HashSet<CustomerAttribute>();
	
	@Column(name="CUSTOMER_GENDER", length=1, nullable=true)
	@Enumerated(value = EnumType.STRING)
	private CustomerGender gender;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CUSTOMER_DOB")
	private Date dateOfBirth;
	
	@Email
	@NotEmpty
	@Column(name="CUSTOMER_EMAIL_ADDRESS", length=96, nullable=false)
	private String emailAddress;
	//as 姓名
	@Column(name="CUSTOMER_NICK", length=96)
	private String nick;

	//modify by cy use for defaultid
	@Column(name="INVOICE_ADDRES_DEFAULT", length=100)
	private Long invoiceaddressdefault;
	
	@Column(name="GRADE")
	private Integer grade;
	
	@Column(name="PHONE", length=11)
	private String phone ;
	
	@Column(name="COMPANY")
	private String compnay ;
	
	@Column(name="PROJECT")
	private String project ;
	

	@Column(name="ADDRESS_DEFAULT")
	private Long addressDefault;
	
	
	@Column(name="INVOICE_DEFAULT")
	private Long invoiceDefault;
	

	@Column(name="CUSTOMER_PASSWORD", length=50)
	private String password;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "customer")
	private Set<CustomerImage> images = new HashSet<CustomerImage>();
	
	@Column(name="RELATEIONSHIP")
	private String relationshipTel;
	/**use for un active user
	 *0:unactive
	 *1:waiting user verify
	 *2:freeze
	 *3:active  
	 */
	@Column(name="CUSTOMER_ANONYMOUS")
	private int anonymous=0;
	
	@Column(name="RECIEVE_EMAIL")
	private boolean recieveEmail=true;
	
	@Column(name="RECIEVE_PHONE")
	private boolean recievePhone;
	
	//format:8-16
	@Column(name="RECIEVE_PHONETIME")
	private String recievePhoneTime;
	
	@Column(name="POINTS", length=10)
	private String points; //member points
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
	private Set<CustomerAddress> addresss = new HashSet<CustomerAddress>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
	private Set<MemberPoints> memberPoints = new HashSet<MemberPoints>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "customer")
	private Set<CustomerInvoice> invoices = new HashSet<CustomerInvoice>();
	
	

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
	@JoinColumn(name = "LANGUAGE_ID", nullable=false)
	private Language defaultLanguage;
	


	@OneToMany(mappedBy = "customer", targetEntity = ProductReview.class)
	private List<ProductReview> reviews = new ArrayList<ProductReview>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", nullable=false)
	private MerchantStore merchantStore;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "CUSTOMER_GROUP", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "CUSTOMER_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private List<Group> groups = new ArrayList<Group>();
	
	@Column(name="RELATE_SALES_PHONE")
	private String relatedSalesMobile;
	
	@Column(name="RELATE_SALES_EMAIL")
	private String relatedSalesEmail;
	
	@Transient
	private String showCustomerStateList;
	
	@Transient
	private String showBillingStateList;
	
	@Transient
	private String showDeliveryStateList;
	
	@Column(name="CUSTOMER_DISCOUNT")
	private Double discount;
	
	
	@Column(name="ACCOUT_TYPE", columnDefinition="varchar(255) default 'research' ", nullable=false)
	private String accountType  = GlobalConstants.BuyerType_ResearchWorker;
	
	@Column(name="ACCOUNT_STATE", columnDefinition="varchar(255) default 'certified' ", nullable=false)
	private String accountState = GlobalConstants.AccountState_NotCertified;;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateOfBirth() {
		return CloneUtils.clone(dateOfBirth);
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = CloneUtils.clone(dateOfBirth);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}

	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}

	public MerchantStore getMerchantStore() {
		return merchantStore;
	}
	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}
	public String getShowCustomerStateList() {
		return showCustomerStateList;
	}

	public void setShowCustomerStateList(String showCustomerStateList) {
		this.showCustomerStateList = showCustomerStateList;
	}

	public String getShowBillingStateList() {
		return showBillingStateList;
	}

	public void setShowBillingStateList(String showBillingStateList) {
		this.showBillingStateList = showBillingStateList;
	}

	public String getShowDeliveryStateList() {
		return showDeliveryStateList;
	}

	public void setShowDeliveryStateList(String showDeliveryStateList) {
		this.showDeliveryStateList = showDeliveryStateList;
	}
	
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public void setAttributes(Set<CustomerAttribute> attributes) {
		this.attributes = attributes;
	}

	public Set<CustomerAttribute> getAttributes() {
		return attributes;
	}

	public void setGender(CustomerGender gender) {
		this.gender = gender;
	}

	public CustomerGender getGender() {
		return gender;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	public Long getInvoiceaddressdefault() {
		return invoiceaddressdefault;
	}

	public void setInvoiceaddressdefault(Long invoiceaddressdefault) {
		this.invoiceaddressdefault = invoiceaddressdefault;
	}
	
	public Set<CustomerInvoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<CustomerInvoice> invoices) {
		this.invoices = invoices;
	}


	public Set<CustomerAddress> getAddresss() {
		return addresss;
	}

	public void setAddresss(Set<CustomerAddress> addresss) {
		this.addresss = addresss;
	}
	
	public Long getAddressDefault() {
		return addressDefault;
	}

	public void setAddressDefault(Long addressDefault) {
		this.addressDefault = addressDefault;
	}

	public Long getInvoiceDefault() {
		return invoiceDefault;
	}

	public void setInvoiceDefault(Long invoiceDefault) {
		this.invoiceDefault = invoiceDefault;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompnay() {
		return compnay;
	}

	public void setCompnay(String compnay) {
		this.compnay = compnay;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public boolean isRecieveEmail() {
		return recieveEmail;
	}

	public void setRecieveEmail(boolean recieveEmail) {
		this.recieveEmail = recieveEmail;
	}

	public boolean isRecievePhone() {
		return recievePhone;
	}

	public void setRecievePhone(boolean recievePhone) {
		this.recievePhone = recievePhone;
	}

	public String getRecievePhoneTime() {
		return recievePhoneTime;
	}

	public void setRecievePhoneTime(String recievePhoneTime) {
		this.recievePhoneTime = recievePhoneTime;
	}

	public int getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(int anonymous) {
		this.anonymous = anonymous;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public Set<MemberPoints> getMemberPoints() {
		return memberPoints;
	}

	public String getRelationshipTel() {
		return relationshipTel;
	}

	public void setRelationshipTel(String relationshipTel) {
		this.relationshipTel = relationshipTel;
	}

	public void setMemberPoints(Set<MemberPoints> memberPoints) {
		this.memberPoints = memberPoints;
	}

	public Set<CustomerImage> getImages() {
		return images;
	}

	public void setImages(Set<CustomerImage> images) {
		this.images = images;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public String getRelatedSalesMobile() {
		return relatedSalesMobile;
	}

	public void setRelatedSalesMobile(String realtedSalesMobile) {
		this.relatedSalesMobile = realtedSalesMobile;
	}

	public String getRelatedSalesEmail() {
		return relatedSalesEmail;
	}

	public void setRelatedSalesEmail(String realtedSalesEmail) {
		this.relatedSalesEmail = realtedSalesEmail;
	}
	
	

}
