document.querySelector("footer").innerHTML = new Date().getFullYear()

document.querySelectorAll("a").forEach(function(at){
    at.classList.add("animated-link");
});

document.addEventListener("DOMContentLoaded", function() {
    var links = document.querySelectorAll('.animated-link');
    
    links.forEach(function(link) {
      link.addEventListener('mouseover', function() {
        this.classList.add('active');
      });
      
      link.addEventListener('mouseout', function() {
        this.classList.remove('active');
      });
    });
  });
  