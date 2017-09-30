/**
 * 
 */
package com.salesmanager.core.business.content.model;

/**
 * Enum defining type of static content.
 * Currently following type of static content can be store and managed within 
 * Shopizer CMS system
 * <pre>
 * 1. Static content like JS, CSS file etc
 * 2. Digital Data (audio,video)
 * </pre>
 * 
 * StaticContentType will be used to distinguish between Digital data and other type of static data
 * stored with in the system.
 * 
 * @author Umesh Awasthi
 * @since 1.2
 *
 */
public enum FileContentType
{
	//add PRODUCT_CERTIFICATE, PRODUCT_PROOF, PRODUCT_THIRDPROOF by sam at 20150807 for more types
	//add STORELICENCE 营业执照
  STATIC_FILE, IMAGE, LOGO, PRODUCT, PRODUCTLG, PROPERTY, MANUFACTURER, PRODUCT_DIGITAL, 
  PRODUCT_CERTIFICATE, PRODUCT_PROOF, PRODUCT_THIRDPROOF, PRODUCT_SELFPROOF, 
  STORELICENCE
}
