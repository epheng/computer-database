package controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import dto.CompanyDTO;
import dto.ComputerDTO;
import mapper.CompanyMapper;
import mapper.ComputerMapper;
import model.Company;
import model.Computer;
import service.DatabaseService;

@Controller
public class ControllerCDB {
	
	@Autowired
	ComputerMapper computerMapper;
	
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	DatabaseService service;
	
	int nbComputerPerPage = 10;
	int nbPage = 1;
	
	public List<ComputerDTO> initComputerDtoList(List<Computer> computerList) {
		List<ComputerDTO> computerDtoList = null;
		computerDtoList = new ArrayList<ComputerDTO>();
		for(Computer c : computerList) {
			computerDtoList.add(computerMapper.toComputerDTO(c));
		}
		return computerDtoList;
	}
	
	public List<CompanyDTO> initCompanyDtoList(List<Company> companyList) {
		List<CompanyDTO> companyDtoList = null;
		companyDtoList = new ArrayList<CompanyDTO>();
		for(Company c : companyList) {
			companyDtoList.add(companyMapper.toCompanyDTO(c));
		}
		return companyDtoList;
	}
	
	public void setPagination(String page, String length) {
		nbPage = page == null ? nbPage : Integer.parseInt(page);
		nbComputerPerPage = length == null ? nbComputerPerPage : Integer.parseInt(length);
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboard(ModelMap model,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "length", required = false) String length) {
		setPagination(page, length);
		List<Computer> computerList = service.findComputers(nbPage, nbComputerPerPage);
		List<ComputerDTO> computerDtoList = initComputerDtoList(computerList);
		int totalNbComputers = service.countComputers();		
		List<Company> companyList = service.findAllCompanies();
		List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
		int nbPagination = totalNbComputers % nbComputerPerPage == 0 ?
				totalNbComputers / nbComputerPerPage :totalNbComputers / nbComputerPerPage + 1;
		model.addAttribute("companies", companyDtoList);		
		model.addAttribute("computerDtoList", computerDtoList);
		model.addAttribute("nbComputer", totalNbComputers);
		model.addAttribute("currentNbPage", nbPage);
		model.addAttribute("nbPagination", nbPagination);
		return "dashboard";
	}
	
	@RequestMapping(value = "/editComputer", method = RequestMethod.GET)
	public String editComputer(ModelMap model,
			@RequestParam(value = "id") int id) {
		Computer computer = service.findComputerById(id);
		ComputerDTO computerDto = computerMapper.toComputerDTO(computer);
		List<Company> companyList = service.findAllCompanies();
		List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
		model.addAttribute("computer", computerDto);
		model.addAttribute("companies", companyDtoList);
		model.addAttribute("computerDto", new ComputerDTO());
		return "editComputer";
	}
	
	@RequestMapping(value = "/addComputer", method = RequestMethod.GET)
	public String addComputer(ModelMap model) {
		List<Company> companyList = service.findAllCompanies();
		List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
		model.addAttribute("companies", companyDtoList);
		model.addAttribute("computerDto", new ComputerDTO());
		return "addComputer";
	}
	
	@RequestMapping(value = "/addComputer", method = RequestMethod.POST)
	public String createComputer(@ModelAttribute("computerDto") @Valid ComputerDTO computerDto, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			List<Company> companyList = service.findAllCompanies();
			List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
			model.addAttribute("companies", companyDtoList);
			return "addComputer";
		} else {
			service.addComputer(computerMapper.toComputer(computerDto));
			return "redirect:dashboard";
		}
	}

	@RequestMapping(value = "/editComputer", method = RequestMethod.POST)
	public String updateComputer(@ModelAttribute("computerDto") @Valid ComputerDTO computerDto, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			List<Company> companyList = service.findAllCompanies();
			List<CompanyDTO> companyDtoList = initCompanyDtoList(companyList);
			model.addAttribute("companies", companyDtoList);
			return "editComputer";
		} else {
			service.updateComputer(computerMapper.toComputer(computerDto));
			return "redirect:dashboard";
		}
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
	public String deleteComputers(@RequestParam(value = "selection") String selection) {
		service.deleteComputers(selection);
		return "redirect:dashboard";
	}
	
	@RequestMapping(value = "/dashboard", params = "action=search", method = RequestMethod.POST)
	public String searchComputers(ModelMap model,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "length", required = false) String length,
			@RequestParam(value = "search") String search) {
		setPagination(page, length);
		List<Computer> computerList = service.searchComputersByNameOrCompany(search);
		List<ComputerDTO> computerDtoList = initComputerDtoList(computerList);
		int totalNbComputers = service.countComputers();
		model.addAttribute("computerDtoList", computerDtoList);
		model.addAttribute("nbComputer", totalNbComputers);
		model.addAttribute("currentNbPage", nbPage);
		model.addAttribute("nbPagination", totalNbComputers % nbComputerPerPage == 0 ?
				totalNbComputers / nbComputerPerPage : totalNbComputers / nbComputerPerPage + 1);
		return "dashboard";
	}
	
	@RequestMapping(value = "/dashboard", params = "action=deleteCompany", method = RequestMethod.POST)
	public String deleteCompany(@RequestParam(value = "selectionCompany") String selectionCompany) {
		service.deleteCompany(selectionCompany);
		return "redirect:dashboard";
	}
	
}
