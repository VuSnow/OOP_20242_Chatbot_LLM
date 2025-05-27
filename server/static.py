import json
from collections import Counter, defaultdict
import pandas as pd

def flatten(d, parent_key='', sep='.'):
    items = []
    for k, v in d.items():
        new_key = f"{parent_key}{sep}{k}" if parent_key else k
        if isinstance(v, dict):
            items.extend(flatten(v, new_key, sep=sep).items())
        else:
            items.append((new_key, v))
    return dict(items)

def analyze_json(json_path):
    with open(json_path, encoding='utf-8') as f:
        data = json.load(f)
    if isinstance(data, dict):
        data = [data]  # support single-product file

    n_products = len(data)
    print(f"Tổng số sản phẩm: {n_products}\n")

    # Thống kê các trường top-level
    top_level_keys = Counter()
    for prod in data:
        top_level_keys.update(prod.keys())
    print("Các trường top-level và số lượng xuất hiện:")
    for k, v in top_level_keys.items():
        print(f"  {k}: {v}/{n_products}")

    # Thống kê trường quan trọng
    important_keys = ["name", "url", "attributes", "categories", "review", "filterable", "Mô tả sản phẩm"]
    print("\nThống kê trường quan trọng:")
    for k in important_keys:
        cnt = sum(1 for prod in data if k in prod and prod[k])
        print(f"  {k}: {cnt}/{n_products}")

    # Thống kê chi tiết attributes
    all_attr_keys = Counter()
    attr_nonempty_counts = defaultdict(int)
    attr_total = 0

    for prod in data:
        attrs = prod.get("attributes", {})
        if not isinstance(attrs, dict): continue
        attr_total += 1
        for k, v in attrs.items():
            all_attr_keys[k] += 1
            if v not in [None, "", "no_selection", [], False]:
                attr_nonempty_counts[k] += 1

    print(f"\nTrung bình số trường trong attributes mỗi sản phẩm: {all_attr_keys.total() / (attr_total or 1):.1f}")
    print("Thống kê trường trong attributes (có dữ liệu thực):")
    rows = []
    for k, total in all_attr_keys.items():
        nonempty = attr_nonempty_counts[k]
        rows.append({"Trường": k, "Có dữ liệu": nonempty, "Tổng số SP có trường này": total, "Tỷ lệ có dữ liệu (%)": f"{100*nonempty/(total or 1):.1f}"})
    df_attr = pd.DataFrame(rows).sort_values(by="Có dữ liệu", ascending=False)
    print(df_attr.to_markdown(index=False))

    # Thống kê categories
    n_categories = sum(1 for prod in data if "categories" in prod and prod["categories"])
    print(f"\nSố sản phẩm có danh mục (categories): {n_categories}/{n_products}")

    # Thống kê review
    n_review = sum(1 for prod in data if "review" in prod and prod["review"])
    print(f"Số sản phẩm có đánh giá (review): {n_review}/{n_products}")

    # Thống kê trường "Mô tả sản phẩm"
    n_desc = sum(1 for prod in data if "Mô tả sản phẩm" in prod and prod["Mô tả sản phẩm"])
    print(f"Số sản phẩm có mô tả sản phẩm: {n_desc}/{n_products}")

    # Lưu ra file Excel
    df_attr.to_excel("thong_ke_attributes.xlsx", index=False)
    print("Đã lưu bảng thống kê chi tiết các trường attributes ra file thong_ke_attributes.xlsx")

    # Trả về bảng chi tiết (nếu muốn lưu ra file hoặc dùng tiếp)
    return {
        "n_products": n_products,
        "top_level_keys": dict(top_level_keys),
        "attr_detail": df_attr,
    }

# Sử dụng
if __name__ == "__main__":
    analyze_json("cellphones_products_with_desc.json")  # Đổi sang tên file json thực tế của bạn
