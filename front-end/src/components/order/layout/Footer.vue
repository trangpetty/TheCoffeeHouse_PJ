<template>
  <footer>
    <div class="container text-white">
      <div class="row">
        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12 col-lg-push-6 col-md-push-6 footer_col">
          <h3 class="footer_title">Giới thiệu</h3>
          <ul class="footer_linklists">
            <li v-for="(item, index) in linkListIntro" :key="index">
              <a :href="item.link" :title="item.title">{{item.title}}</a>
            </li>
          </ul>
        </div>
        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12 col-lg-push-6 col-md-push-6 footer_col">
          <h3 class="footer_title">Điều khoản</h3>
          <ul class="footer_linklists">
            <li v-for="(item, index) in linkList" :key="index">
              <a :href="item.link" :title="item.title">{{item.title}}</a>
            </li>
          </ul>
        </div>
        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12 col-lg-push-6 col-md-push-6 footer_col">
          <div class="hotline-footer">
            <p>
              <font-awesome-icon icon="fa-solid fa-phone" />
              Đặt hàng: 1800 6936
            </p>
            <p>
              <font-awesome-icon icon="fa-solid fa-location-dot" />
              Liên hệ
            </p>
          </div>
          <div class="address_footer">
            <p>
              Tầng 3-4 Hub Building <br>
              195/10E Điện Biên Phủ, P.15, <br>
              Q.Bình Thạnh, TP.Hồ Chí Minh
            </p>
          </div>
        </div>
        <div class="col-lg-3 col-md-3 col-sm-6 col-xs-12 col-lg-push-6 col-md-push-6 footer_col">
<!--          <div class="fb_iframe_widget">-->
<!--            <span style="vertical-align: bottom; width: 270px; height: 130px;">-->
<!--              <iframe src="https://www.facebook.com/profile.php?id=100005014935213" width="1000px" height="1000px"></iframe>-->
<!--            </span>-->
<!--          </div>-->
          <ul class="social_links">
            <li>
              <a href="#">
                <img src="https://file.hstatic.net/1000075078/file/test_fd4e30831d14438f9d0f1fcf255dfadb.svg" alt="">
              </a>
            </li>
            <li>
              <a href="#">
                <img src="https://file.hstatic.net/1000075078/file/test_db37bb3840ac45f3a0a3cf7aa8a57d9e.svg" alt="">
              </a>
            </li>
          </ul>
        </div>
      </div>

      <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 footer_col">
          <hr>
        </div>
        <div class="col-lg-9 col-md-9 col-sm-9 col-xs-12 footer_col">
          <ul class="footer_itemlists">
            <li>Công ty cổ phần thương mại dịch vụ Trà Cà Phê VN</li>
            <li>Mã số DN: 0312867172 do sở kế hoạch và đầu tư TP.Hà Nội cấp ngày 22/07/2024. Người đại diện: Lê Thị Thùy Trang</li>
            <li>Địa chỉ: 55 Giải Phóng, P.Đồng Tâm, Q. Hai Bà Trưng, TP.Hà Nội   Điện thoại: (028) 7107 8079   Email: trang200164@huce.edu.vn</li>
            <li>© 2014-2022 Công ty cổ phần thương mại dịch vụ Trà Cà Phê VN mọi quyền bảo lưu</li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>

<script lang="ts" setup>
import {ref} from 'vue';

const linkListIntro = ref([
  {link: "#", title: "Về Chúng Tôi"},
  {link: "#", title: "Sản phẩm"},
  {link: "#", title: "Khuyến mãi"},
  {link: "#", title: "Chuyện cà phê"},
  {link: "#", title: "Cửa Hàng"},
  {link: "#", title: "Tuyển dụng"},
]);

const linkList = ref([
  {link: "#", title: "Điều khoản sử dụng"},
  {link: "#", title: "Chính sách bảo mật thông tin"},
  {link: "#", title: "Hướng dẫn xuất hóa đơn GTGT"}
]);

</script>

<style scoped>
footer {
  background: #000000D9;
  padding-top: 34px;
  padding-bottom: 54px;
}

.hotline-footer p {
  font-weight: 600;
  margin-bottom: 24px;
  color: #fff;
}

.hotline-footer p svg {
  margin-right: 8px;
}

.footer_title {
  font-weight: 600;
  font-size: 14px;
  color: #fff;
  margin-top: 0;
  margin-bottom: 16px;
}

.footer_linklists {
  padding: 0;
  margin: 0;
}

.footer_linklists li {
  margin-bottom: 8px;
  font-size: 12px;
}

.footer_linklists li a {
  color: #fff;
  line-height: 20px;
  font-family: 'Lato', sans-serif;
  text-decoration: none;
  transition: .5s;
}

.address_footer {
  margin-top: 30px;
}

.address_footer p {
  color: #fff;
  line-height: 20px;
  margin: 0;
  font-size: 12px;
}

.fb_iframe_widget {
  display: inline-block;
  position: relative;
}

iframe {
  border: none;
  visibility: visible;
  width: 270px;
  height: 130px;
}

.social_links {
  display: flex;
  padding: 0;
  margin-top: 18px;
}

.social_links > li {
  margin-right: 13px;
}


hr {
  margin-top: 20px;
  margin-bottom: 20px;
  border: 0;
  border-top: 1px solid #eee;
  opacity: 1;
}

.footer_itemlists {
  padding: 0;
  margin: 0;
}

ul {
  list-style-type: none;
}

.footer_itemlists li {
  font-size: 12px;
  color: #fff;
  line-height: 18px;
  font-family: 'Lato', sans-serif;
}

@media (min-width: 1200px) {
  .col-lg-push-6 {
    left: 50%;
  }
  .col-lg-3 {
    width: 25%;
  }
}
</style>