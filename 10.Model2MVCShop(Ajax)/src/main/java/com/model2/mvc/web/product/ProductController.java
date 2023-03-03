package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	//@RequestMapping("/addProduct.do")
	//public String addProduct( @ModelAttribute("product") Product product, Model model ) throws Exception {
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct( @ModelAttribute("product") Product product, Model model ) throws Exception {
		
		System.out.println("/product/addProduct : POST");
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		
		//Business Logic
		productService.insertProduct(product);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		
		return "forward:/product/addProduct.jsp";
	}
	
	//@RequestMapping("/getProduct.do")
	//public String getProduct(@RequestParam("prodNo") String prodNo, Model model) throws Exception{
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct(@RequestParam("prodNo") String prodNo, Model model) throws Exception{
			
		System.out.println("getProduct");
		//Business Logic
		Product product = productService.findProduct(Integer.parseInt(prodNo));
		//Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	//@RequestMapping("/listProduct.do")
	@RequestMapping(value="listProduct")
	public String listProduct(@RequestParam("menu") String menu,  @ModelAttribute("search") Search search, Model model, HttpServletRequest request) throws Exception{
		System.out.println("listProduct");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String, Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		if(menu.equals("manage"))
		return "forward:/product/listProduct.jsp";
		else
		return "forward:/product/listProductSearch.jsp";
	}
	
	//@RequestMapping("/updateProduct.do")
	//public String updateProduct( @ModelAttribute("product") Product product, Model model , HttpSession session) throws Exception{
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product, Model model , HttpSession session) throws Exception{
		
		System.out.println("/updateProduct");
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		
		productService.updateProduct(product);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo();
	}
	
	
	//@RequestMapping("/updateProductView.do")
	//public String updateProductView( @RequestParam("prodNo") String prodNo, Model model) throws Exception{
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") String prodNo, Model model) throws Exception{
		System.out.println("/updateProductView");
		
		Product product = productService.findProduct(Integer.parseInt(prodNo));
		//Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	
}
