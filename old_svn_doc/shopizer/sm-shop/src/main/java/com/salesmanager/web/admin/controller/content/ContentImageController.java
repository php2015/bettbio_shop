package com.salesmanager.web.admin.controller.content;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.InputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.content.ContentFiles;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;

/**
 * Manage static content type image
 * - Add images
 * - Remove images
 * @author Carl Samson
 *
 */
@Controller
public class ContentImageController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentImageController.class);
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * Entry point for the file browser used from the javascript
	 * content editor
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value={"/admin/content/fileBrowser.html"}, method=RequestMethod.GET)
	public String displayFileBrowser(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		
		return ControllerConstants.Tiles.ContentImages.fileBrowser;
		
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/content/image/upload.html", method=RequestMethod.POST, produces="application/json")	
	public @ResponseBody String saveProductImage(HttpServletRequest request, HttpServletResponse response,Locale locale) throws Exception{
		AjaxResponse resp = new AjaxResponse();
		try{
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			 MultipartHttpServletRequest mureq = (MultipartHttpServletRequest) request;
			  
			 String pId = request.getParameter("pid");
			 String fname = request.getParameter("fname");
			 MultipartFile file = mureq.getFile(pId);
			 
			//response.setCharacterEncoding("utf-8");
			//String filename=file.getOriginalFilename();
		//	file.getOriginalFilename();
			//String callback = request.getParameter("CKEditorFuncNum");
			String fileSuffix = StringUtils.substring(fname, fname.lastIndexOf(".") + 1);
			if (!StringUtils.equalsIgnoreCase(fileSuffix, "jpg")
	                && !StringUtils.equalsIgnoreCase(fileSuffix, "jpeg")
	                && !StringUtils.equalsIgnoreCase(fileSuffix, "bmp")
	                && !StringUtils.equalsIgnoreCase(fileSuffix, "gif")
	                && !StringUtils.equalsIgnoreCase(fileSuffix, "png")){		
				//return "<script type=\"text/javascript\">alert('Image file format is not correct,must be jpg|jpeg|bmp|gif|png');</script>";
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_VALIDATION_FAILED);
			}
			
			final List<InputContentFile> contentImagesList=new ArrayList<InputContentFile>();
			 InputContentFile cmsContentImage = new InputContentFile();
	        // cmsContentImage.setFileName( RadomSixNumber.getImageName(filename));
			 cmsContentImage.setFileName(fname);
	         cmsContentImage.setMimeType( file.getContentType() );
	         cmsContentImage.setFile(file.getInputStream());
	         cmsContentImage.setFileContentType(FileContentType.IMAGE);
	         contentImagesList.add( cmsContentImage);        
	         contentService.addContentFiles( store.getCode(), contentImagesList );
	         contentService.getContentFilesNames(store.getCode(), FileContentType.IMAGE);
	         contentService.getContentFiles(store.getCode(), FileContentType.IMAGE);
	         resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		}catch (Exception e){
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
         //String imgpath = new StringBuilder().append("/static").append("/").append(store.getCode()).append("/").append(FileContentType.IMAGE.name()).append("/")
			//	.append(cmsContentImage.getFileName()).toString();	
		
	    //return "<script type='text/javascript'>" + "window.parent.CKEDITOR.tools.callFunction(" + callback               + ",'" + request.getContextPath()+imgpath + "',''" + ")"+"</script>";
		
	}
	
	/**
	 * Get images for a given merchant store
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value={"/admin/content/contentImages.html","/admin/content/contentManagement.html"}, method=RequestMethod.GET)
	public String getContentImages(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		return ControllerConstants.Tiles.ContentImages.contentImages;
		
	}
	
	
	@SuppressWarnings({ "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/content/images/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody List<String> pageImages(HttpServletRequest request, HttpServletResponse response) {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<String> inames;
		try {
			inames = contentService.getContentFilesNames(store.getCode(), FileContentType.IMAGE);
		} catch (ServiceException e) {			
			return null;
		}
		return inames;
	}
	
	/**
	 * Controller methods which allow Admin to add content images to underlying
	 * Infinispan cache.
	 * @param model model object
	 * @param request http request object
	 * @param response http response object
	 * @return view allowing user to add content images
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value="/admin/content/createContentImages.html", method=RequestMethod.GET)
    public String displayContentImagesCreate(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
      
	    return ControllerConstants.Tiles.ContentImages.addContentImages;

    }
	
	/**
	 * Method responsible for adding content images to underlying Infinispan cache.
	 * It will add given content image(s) for given merchant store in the cache.
	 * Following steps will be performed in order to add images
	 * <pre>
	 * 1. Validate form data
	 * 2. Get Merchant Store based on merchant Id.
	 * 3. Call {@link InputContentFile} to add image(s).
	 * </pre>
	 * 
	 * @param contentImages
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value="/admin/content/saveContentImages.html", method=RequestMethod.POST)
	public String saveContentImages(@ModelAttribute(value="contentFiles") @Valid final ContentFiles contentImages, final BindingResult bindingResult,final Model model, final HttpServletRequest request) throws Exception{
	    
		this.setMenu(model, request);
	    if (bindingResult.hasErrors()) {
	        LOGGER.info( "Found {} Validation errors", bindingResult.getErrorCount());
	       return ControllerConstants.Tiles.ContentImages.addContentImages;
	       
        }
	    final List<InputContentFile> contentImagesList=new ArrayList<InputContentFile>();
        final MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
        if(CollectionUtils.isNotEmpty( contentImages.getFile() )){
            LOGGER.info("Saving {} content images for merchant {}",contentImages.getFile().size(),store.getId());
            for(final MultipartFile multipartFile:contentImages.getFile()){
                if(!multipartFile.isEmpty()){
                    ByteArrayInputStream inputStream = new ByteArrayInputStream( multipartFile.getBytes() );
                    InputContentFile cmsContentImage = new InputContentFile();
                    cmsContentImage.setFileName(multipartFile.getOriginalFilename() );
                    cmsContentImage.setMimeType( multipartFile.getContentType() );
                    cmsContentImage.setFile( inputStream );
                    cmsContentImage.setFileContentType(FileContentType.IMAGE);
                    contentImagesList.add( cmsContentImage);
                }
            }
            
            if(CollectionUtils.isNotEmpty( contentImagesList )){
                contentService.addContentFiles( store.getCode(), contentImagesList );
            }
            else{
                // show error message on UI
            }
        }
       
        return ControllerConstants.Tiles.ContentImages.contentImages;
	}
	
	
	/**
	 * Removes a content image from the CMS
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value="/admin/content/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String imageName = request.getParameter("name");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			

			
			contentService.removeFile(store.getCode(), FileContentType.IMAGE, imageName);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("content", "content");
		activeMenus.put("content-images", "content-images");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("content");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
