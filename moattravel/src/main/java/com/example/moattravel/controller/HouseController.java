package com.example.moattravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.moattravel.entity.House;
import com.example.moattravel.repository.HouseRepository;

@Controller
@RequestMapping("/houses")
public class HouseController {
	private final HouseRepository houseRepository;

	public HouseController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "area", required = false) String area,
			@RequestParam(name = "price", required = false) Integer price,
			@RequestParam(name = "order", required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {

		Page<House> housePage;

		if (keyword != null && !keyword.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			} else {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			}
		} else if (area != null && !area.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
			} else {
				housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
			}
		} else if (price != null) {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
			} else {
				housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
			}
		} else {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
			} else {
				housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
			}
		}

		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("area", area);
		model.addAttribute("price", price);
		model.addAttribute("order", order);

		return "houses/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		model.addAttribute("house", house);
		return "admin/houses/show";
	}

}

/*@GetMapping("/{id}")
public String show(@PathVariable(name = "id") Integer id, Model model) {
	House house = houseRepository.getReferenceById(id);
	model.addAttribute("house", house);
	return "admin/houses/show";
}

@GetMapping("/register")
public String register(Model model) {
	model.addAttribute("houseRegisterForm", new HouseRegisterForm());
	return "admin/houses/register";
}

@PostMapping("/create")
public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
	if (bindingResult.hasErrors()) {
		return "admin/houses/register";
	}

	houseService.create(houseRegisterForm);
	redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");

	return "redirect:/admin/houses";
}

@GetMapping("/{id}/edit")
public String edit(@PathVariable(name = "id") Integer id, Model model) {
	House house = houseRepository.getReferenceById(id);
	String imageName = house.getImageName();
	HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(),
			house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(),
			house.getPhoneNumber());

	model.addAttribute("imageName", imageName);
	model.addAttribute("houseEditForm", houseEditForm);

	return "admin/houses/edit";
}

@PostMapping("/{id}/update")
public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
	if (bindingResult.hasErrors()) {
		return "admin/houses/edit";
	}

	houseService.update(houseEditForm);
	redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");

	return "redirect:/admin/houses";
}

@PostMapping("/{id}/delete")
public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
	houseRepository.deleteById(id);
	redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");

	return "redirect:/admin/houses";
}
}*/
