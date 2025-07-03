function togglePassword(id) {
  const input = document.getElementById(id);
  input.type = (input.type === "password") ? "text" : "password";
}

window.addEventListener("DOMContentLoaded", () => {
  const passwordInput = document.getElementById("password");
  const confirmInput = document.getElementById("confirmPassword");
  const submitBtn = document.getElementById("submitBtn");
  const mismatchMsg = document.getElementById("passwordMismatch");

  function validatePasswords() {
    const pw = passwordInput.value;
    const confirm = confirmInput.value;
    const match = pw === confirm && pw.length >= 8;

    submitBtn.disabled = !match;
    mismatchMsg.classList.toggle("d-none", match);
  }

  passwordInput.addEventListener("input", validatePasswords);
 
});
