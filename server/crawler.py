import requests
import json
import time

GRAPHQL_URL = "https://api.cellphones.com.vn/v2/graphql/query"

headers = {
    "Content-Type": "application/json",
}

GRAPHQL_QUERY = """
            query GetProductsByCateId{
                products(
                        filter: {
                            static: {
                                categories: ["3"],
                                province_id: 30,
                                stock: {
                                   from: 0
                                },
                                stock_available_id: [46, 56, 152, 4920],
                               filter_price: {from:0to:54990000}
                            },
                            dynamic: {
                                
                                
                            }
                        },
                        page: %d,
                        size: 20,
                        sort: [{view: desc}]
                    )
                {
                    general{
                        product_id
                        name
                        attributes
                        sku
                        doc_quyen
                        manufacturer
                        url_key
                        url_path
                        categories{
                            categoryId
                            name
                            uri
                        }
                        review{
                            total_count
                            average_rating
                        }
                    },
                    filterable{
                        is_installment
                        stock_available_id
                        company_stock_id
                        filter {
                           id
                           Label
                        }
                        is_parent
                        price
                        prices
                        special_price
                        promotion_information
                        thumbnail
                        promotion_pack
                        sticker
                        flash_sale_types
                    },
                }
            }
"""

BASE_PRODUCT_URL = "https://cellphones.com.vn/"

def crawl_products():
    all_products = []
    page = 1
    while True:
        print(f"Crawling page {page}")
        # Chèn số trang vào query
        query = GRAPHQL_QUERY % page
        payload = {
            "query": query,
            "variables": {}
        }
        resp = requests.post(GRAPHQL_URL, headers=headers, json=payload)
        if resp.status_code != 200:
            print(f"Lỗi HTTP: {resp.status_code}")
            break
        data = resp.json()
        try:
            products = data['data']['products']
        except Exception:
            print("Không còn sản phẩm hoặc response lỗi.")
            break
        if not products:
            print("Hết sản phẩm.")
            break
        for prod in products:
            g = prod['general']
            f = prod['filterable']
            product_url = BASE_PRODUCT_URL + g['url_path']
            item = {
                "name": g["name"],
                "url": product_url,
                "product_id": g["product_id"],
                "attributes": g["attributes"],
                "categories": g["categories"],
                "review": g.get("review", {}),
                "filterable": f,
            }
            all_products.append(item)
        page += 1
        time.sleep(1)  # tránh bị chặn IP
    print(f"Tổng số sản phẩm: {len(all_products)}")
    
    with open("cellphones_products.json", "w", encoding="utf-8") as f:
        json.dump(all_products, f, ensure_ascii=False, indent=2)

if __name__ == "__main__":
    crawl_products()
