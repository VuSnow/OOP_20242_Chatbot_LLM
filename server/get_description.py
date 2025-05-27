import requests
from bs4 import BeautifulSoup
import json
import time

def get_description_from_url(url):
    try:
        headers = {
            "User-Agent": "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
        }
        resp = requests.get(url, headers=headers, timeout=15)
        if resp.status_code != 200:
            print(f"Lỗi khi truy cập {url}: {resp.status_code}")
            return ""
        soup = BeautifulSoup(resp.text, "lxml")
        content_div = soup.find("div", {"id": "cpsContent"})
        if not content_div:
            print(f"Không tìm thấy mô tả sản phẩm ở {url}")
            return ""
        # Loại bỏ các button, show more, script, style nếu có
        for tag in content_div(["script", "style", "button", "svg"]):
            tag.decompose()
        text = content_div.get_text(separator=" ", strip=True)
        text = ' '.join(text.split())
        return text
    except Exception as e:
        print(f"Lỗi khi lấy description từ {url}: {e}")
        return ""

def add_descriptions_to_products(json_in, json_out):
    with open(json_in, "r", encoding="utf-8") as f:
        products = json.load(f)
    for idx, prod in enumerate(products):
        url = prod.get("url")
        if not url:
            continue
        print(f"[{idx+1}/{len(products)}] Crawling description from {url}")
        description = get_description_from_url(url)
        prod["Mô tả sản phẩm"] = description
        time.sleep(1.5)  # tránh bị chặn IP
    with open(json_out, "w", encoding="utf-8") as f:
        json.dump(products, f, ensure_ascii=False, indent=2)
    print(f"Đã cập nhật description cho {len(products)} sản phẩm -> {json_out}")

if __name__ == "__main__":
    # Đổi file input/output cho phù hợp
    add_descriptions_to_products("cellphones_products.json", "cellphones_products_with_desc.json")
