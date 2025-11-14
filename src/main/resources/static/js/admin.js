document.addEventListener("DOMContentLoaded", function () {
  var fileInput = document.getElementById("imageFile");
  if (!fileInput) return;

  fileInput.addEventListener("change", function (event) {
    var file = event.target.files && event.target.files[0];
    if (!file) {
      return;
    }

    var reader = new FileReader();
    reader.onload = function (e) {
      var preview = document.querySelector(".admin-form__preview");
      if (!preview) {
        preview = document.createElement("div");
        preview.className = "admin-form__preview";
        var group = fileInput.closest(".admin-form__group");
        if (group) {
          group.appendChild(preview);
        }
      }

      preview.innerHTML = "";
      var label = document.createElement("span");
      label.textContent = "Vista previa";
      var img = document.createElement("img");
      img.src = e.target.result;
      img.alt = "Vista previa de la imagen";

      preview.appendChild(label);
      preview.appendChild(img);
    };

    reader.readAsDataURL(file);
  });
});
