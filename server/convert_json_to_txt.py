import json
from bs4 import BeautifulSoup

# Mapping từ key không dấu sang tiếng Việt có dấu (hoặc đẹp hơn)
KEY_VI_MAP = {
    "name": "Tên sản phẩm",
    "manufacturer": "Hãng sản xuất",
    "battery": "Dung lượng pin",
    "chipset": "Chipset",
    "cpu": "CPU",
    "display_size": "Kích thước màn hình",
    "display_resolution": "Độ phân giải màn hình",
    "camera_primary": "Camera chính",
    "camera_secondary": "Camera trước",
    "storage": "Bộ nhớ trong",
    "memory_internal": "RAM",
    "operating_system": "Hệ điều hành",
    "os_version": "Phiên bản hệ điều hành",
    "screen_size": "Kích thước màn hình",
    "sim": "SIM",
    "weight": "Khối lượng",
    "dimensions": "Kích thước",
    "mobile_nhu_cau_sd": "Nhu cầu sử dụng",
    "mobile_tinh_nang_dac_biet": "Tính năng nổi bật",
    "warranty_information": "Bảo hành",
    "promotion_information": "Khuyến mãi",
    "key_selling_points": "Đặc điểm nổi bật",
    "product_weight": "Khối lượng",
    "mobile_chip_filter": "Dòng chip",
    "mobile_camera_feature": "Tính năng camera",
    "mobile_display_features": "Tính năng màn hình",
    "mobile_type_of_display": "Loại màn hình",
    "mobile_kieu_man_hinh": "Kiểu màn hình",
    "mobile_storage_filter": "Lựa chọn bộ nhớ",
    "mobile_ram_filter": "Lựa chọn RAM",
    "mobile_cong_nghe_sac": "Công nghệ sạc",
    "mobile_cong_sac": "Cổng sạc",
    "mobile_nfc": "NFC",
    "mobile_khang_nuoc_bui": "Chuẩn kháng nước/bụi",
    "mobile_jack_tai_nghe": "Jack tai nghe",
    "mobile_hong_ngoai": "Hồng ngoại",
    "mobile_chat_lieu_khung_vien": "Chất liệu khung viền",
    "mobile_chat_lieu_mat_lung": "Chất liệu mặt lưng",
    "wlan": "Kết nối không dây",
    "tax_vat": "VAT",
    "sim_special_group": "Ưu đãi SIM",
    "included_accessories": "Phụ kiện đi kèm",
    "promotion_percent": "Mức giảm giá (%)",
    "short_description": "Mô tả ngắn",
    "review": "Đánh giá",
    "categories": "Phân loại",
    "bao_hanh_1_doi_1": "Bảo hành 1 đổi 1",
    "gia_vo_doi": "Giá vô đổi",
    "tablet_tien_ich": "Tính năng tablet",
    "tablet_chat_lieu_khung_vien": "Chất liệu khung viền",
    "tablet_chat_lieu_mat_lung": "Chất liệu mặt lưng",
    "tablet_kieu_man_hinh": "Kiểu màn hình",
    "tien_coc": "Tiền cọc",
    "mobile_tan_so_quet": "Tần số quét",
    "mobile_tinh_nang_camera": "Tính năng camera",
    "mobile_ra_mat": "Ra mắt",
    "sac_khong_day": "Sạc không dây",
    "sac_khong_day_text": "Sạc không dây text",
    "mobile_quay_video_truoc": "Quay video trước",
    "mobile_type_of_display": "Loại màn hình",
    "operating_system": "Hệ điều hành",
    "product_state": "Trạng thái sản phẩm",
    "product_weight": "Khối lượng",
    "tablet_tuong_thich": "Tương thích",
    "thu_phong_quang_hoc": "Thu phong quang học",
    "warranty_information": "Thông tin bảo hành",
    "Mô tả sản phẩm": "Mô tả sản phẩm",
    
    # ... bổ sung thêm nếu cần, hoặc cho phép fallback tự động
}

def get_best_price(prices):
    if not prices or not isinstance(prices, dict):
        return ""
    # Map tên key sang tiếng Việt ưu tiên và hiển thị
    priority_keys = [
        "special", "root", "smem", "svip"
    ]
    name_map = {
        "root": "Giá niêm yết",
        "special": "Giá khuyến mãi",
        "smem": "Giá thành viên",
        "svip": "Giá VIP",
        "smem_student": "Giá thành viên (Học sinh)",
        "smem_teacher": "Giá thành viên (Giáo viên)",
        "snew_student": "Giá thành viên mới (Học sinh)",
        "snew_teacher": "Giá thành viên mới (Giáo viên)",
        "snull_student": "Giá không phân loại (Học sinh)",
        "snull_teacher": "Giá không phân loại (Giáo viên)",
        "svip_student": "Giá VIP (Học sinh)",
        "svip_teacher": "Giá VIP (Giáo viên)",
    }
    price_strs = []

    # Ưu tiên các giá phổ biến lên đầu (nếu có)
    for k in priority_keys:
        v = prices.get(k)
        if v is None or not isinstance(v, dict):
            continue
        price = v.get('value')
        discount = v.get('discount_value', 0)
        if price is None:
            continue
        key_name = name_map.get(k, f"Giá {k}")
        if discount and int(discount) > 0:
            price_strs.append(f"{key_name}: {int(price):,} VNĐ (giảm {int(discount):,} VNĐ)")
        else:
            price_strs.append(f"{key_name}: {int(price):,} VNĐ")

    # Thêm tất cả các giá còn lại (không trùng)
    for k, v in prices.items():
        if k in priority_keys:
            continue
        if v is None or not isinstance(v, dict):
            continue
        price = v.get('value')
        discount = v.get('discount_value', 0)
        if price is None:
            continue
        key_name = name_map.get(k, f"Giá {k}")
        if discount and int(discount) > 0:
            price_strs.append(f"{key_name}: {int(price):,} VNĐ (giảm {int(discount):,} VNĐ)")
        else:
            price_strs.append(f"{key_name}: {int(price):,} VNĐ")

    return "\n".join(price_strs)


def is_noise_field(fieldname):
    # Loại các trường không nên đưa vào mô tả text
    blacklist = [
        "image", "icon", "ads", "label", "base_image", "url", "thumbnail", 
        "doc_quyen", "id", "uuid", "external_id"
    ]
    return any(bad in fieldname for bad in blacklist)

def attr_to_text(attributes):
    lines = []
    for k, v in attributes.items():
        if v in [None, '', [], "no_selection", False]:
            continue
        if is_noise_field(k):
            continue
        key_vn = KEY_VI_MAP.get(k, k.replace("_", " ").capitalize())
        # Xử lý field là HTML
        if isinstance(v, str) and ("<li>" in v or "<br" in v or "<div" in v or "<ul" in v or "<p" in v):
            v = BeautifulSoup(v, features="html.parser").get_text(separator=', ', strip=True)
        lines.append(f"{key_vn}: {v}")
    return "\n".join(lines)

def product_to_text(product):
    lines = []
    a = product.get('attributes', {})
    f = product.get('filterable', {})

    # Tên sản phẩm
    lines.append(f"{KEY_VI_MAP.get('name', 'Tên sản phẩm')}: {product.get('name', '')}")
    # Link sản phẩm
    if product.get("url"):
        lines.append(f"Link: {product['url']}")
    # Giá các loại
    prices = f.get('prices', {})
    price_text = get_best_price(prices)
    if price_text:
        lines.append(price_text)
    # Tổng hợp attributes thành text
    attr_text = attr_to_text(a)
    if attr_text:
        lines.append(attr_text)

    for desc_key in ("Mô tả sản phẩm", "description", "desc"):
        if product.get(desc_key):
            lines.append(f"{KEY_VI_MAP.get(desc_key, desc_key)}: {product[desc_key]}")
            break
    # Nhóm filter đặc biệt (ví dụ nhu cầu, tính năng nổi bật) nếu có thể tách riêng
    if a.get('mobile_nhu_cau_sd'):
        lines.append(f"{KEY_VI_MAP.get('mobile_nhu_cau_sd', 'Nhu cầu sử dụng')}: {a.get('mobile_nhu_cau_sd')}")
    if a.get('mobile_tinh_nang_dac_biet'):
        lines.append(f"{KEY_VI_MAP.get('mobile_tinh_nang_dac_biet', 'Tính năng nổi bật')}: {a.get('mobile_tinh_nang_dac_biet')}")
    # Danh mục
    cates = ", ".join([c['name'] for c in product.get('categories', [])])
    if cates:
        lines.append(f"{KEY_VI_MAP.get('categories', 'Phân loại')}: {cates}")
    # Đánh giá
    review = product.get('review', {})
    if review.get('average_rating'):
        lines.append(f"Đánh giá: {review['average_rating']}/5 ({review.get('total_count', 0)} lượt đánh giá)")
    return "\n".join(lines)


def products_json_to_txt(json_path, output_path):
    with open(json_path, "r", encoding="utf-8") as f:
        products = json.load(f)
    all_texts = []
    for idx, prod in enumerate(products):
        prod_text = product_to_text(prod)
        # Separator để chunk dễ dàng
        all_texts.append(f"##### PRODUCT {idx+1}\n{prod_text}\n")
    with open(output_path, "w", encoding="utf-8") as f:
        f.write("\n".join(all_texts))
    print(f"Đã ghi tổng hợp {len(products)} sản phẩm vào {output_path}")

if __name__ == "__main__":
    # Đổi tên file ở đây nếu cần
    products_json_to_txt("cellphones_products_with_desc.json", "cellphones_products_with_desc.txt")
