document.addEventListener("DOMContentLoaded", function () {
  setupImagePreview();
  renderActivityChart();
});

function setupImagePreview() {
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
}

function renderActivityChart() {
  var canvas = document.querySelector(".activity-chart");
  if (!canvas) return;

  var dataAttr = canvas.getAttribute("data-points");
  if (!dataAttr) return;

  try {
    var points = JSON.parse(dataAttr);
    if (!Array.isArray(points) || points.length === 0) {
      return;
    }
    drawSparkline(canvas, points.map(Number).filter(function (n) { return !Number.isNaN(n); }));
  } catch (e) {
    console.warn("No se pudo interpretar la actividad", e);
  }
}

function drawSparkline(canvas, points) {
  if (points.length === 0) return;
  var ctx = canvas.getContext("2d");
  var width = canvas.width = canvas.clientWidth;
  var height = canvas.height = canvas.clientHeight;

  var min = Math.min.apply(null, points);
  var max = Math.max.apply(null, points);
  var range = max - min || 1;
  var step = width / (points.length - 1 || 1);

  ctx.clearRect(0, 0, width, height);

  // grid baseline
  ctx.strokeStyle = "rgba(76, 110, 245, 0.15)";
  ctx.lineWidth = 1;
  ctx.beginPath();
  ctx.moveTo(0, height - 1);
  ctx.lineTo(width, height - 1);
  ctx.stroke();

  ctx.strokeStyle = "#4c6ef5";
  ctx.lineWidth = 2;
  ctx.lineJoin = "round";
  ctx.lineCap = "round";
  ctx.beginPath();
  points.forEach(function (value, index) {
    var x = step * index;
    var y = height - ((value - min) / range) * height;
    if (index === 0) {
      ctx.moveTo(x, y);
    } else {
      ctx.lineTo(x, y);
    }
  });
  ctx.stroke();

  var lastValue = points[points.length - 1];
  ctx.fillStyle = "#4c6ef5";
  ctx.beginPath();
  ctx.arc(width - 3, height - ((lastValue - min) / range) * height, 4, 0, Math.PI * 2);
  ctx.fill();
}
