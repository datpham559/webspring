package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@RequestMapping(value="/categories")
	public String view(Model model) {
		List<Category> listCategories = service.getAll();
		model.addAttribute("listCategories", listCategories);
		return "categories/categories";
	}
	
	@RequestMapping(value = "/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		
		service.deleteCategory(id);
		return "redirect:/categories";
	}
	
	@RequestMapping(value="/categories/new")
	public String createCategory(Model model) {
		
		List<Category> listCategories = service.listCategoriesUsedForm();
		
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Creat New Category");
		model.addAttribute("listCategories", listCategories);

		return "categories/category_form";
		
	}
	
	@RequestMapping(value = "/categories/save")
	public String saveCategory(Category category,
				@RequestParam(name="fileImage" )MultipartFile multipartFile,
				RedirectAttributes redirectAttributes) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		category.setImage(fileName);
		
		Category savedCategory = service.save(category);
		String uploadDir = "../categories-images/"+savedCategory.getId();
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		redirectAttributes.addFlashAttribute("message", "The category has been save");
		return "redirect:/categories";
	}
	
	@RequestMapping(value = "/categories/edit/{id}")
	public String editCatgory(@PathVariable(name ="id") Integer id,RedirectAttributes redirectAttributes,Model model) {
		try {
			Category category = service.get(id);
			List<Category> listCategories = service.listCategoriesUsedForm();
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit Category (ID " + id +")");
			model.addAttribute("listCategories", listCategories);
			return "categories/category_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
			return "redirect:/categories";
		}
		
	}




}
