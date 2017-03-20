package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
public class ProductController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductController(){
		System.out.println(this.getClass());
	}
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception{
		
		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product ) throws Exception{
		
		System.out.println("/addProduct.do");
		//Business Logic
		productService.addProduct(product);
		
		return "forward:/product/addProductResult.jsp";
	}
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo,@RequestParam ("menu")String menu , Model model)throws Exception{
		
		System.out.println("/getProduct.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		//Model과 View의 연결 model.add~~해서 데이터를 저장해주면 jsp 즉,View 에서 받아먹을수있다.
		model.addAttribute("product", product);
		
		if(menu.equals("search")){
		return "forward:/product/getProduct.jsp";
		}else{
			return "forward:/product/updateProductView.jsp";
		}
	}
	@RenderMapping("/updateProdoductView.do")
	public String updateProductView(@RequestParam("prodNo") int prodNo,Model model)throws Exception{
		
		System.out.println("/updateProductView.do");
		//Business Logic
		Product product=productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product,Model model) throws Exception{
		
		System.out.println("/updateProduct.do");
		
		productService.updateProduct(product);
		
		Product product2 =productService.getProduct(product.getProdNo());
		
		model.addAttribute("product", product2);
		
		return "forward:/product/getProduct.jsp";
	}
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search")Search search,Model model,@RequestParam ("menu")String menu, HttpServletRequest request)throws Exception{
		
		System.out.println("/listProduct.do");
		 
		if(search.getCurrentPage()==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String,Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";	
	}
}
