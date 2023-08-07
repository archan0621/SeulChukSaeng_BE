document.addEventListener("DOMContentLoaded", function () {
    const menuIcon = document.querySelector(".menu-icon");
    const menuTab = document.querySelector(".menu-tab");

    menuIcon.addEventListener("click", function () {
        menuTab.classList.toggle("active");
    });
});