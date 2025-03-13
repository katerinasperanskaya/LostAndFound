export default {
  template: `
  <div class="container d-flex justify-content-center align-items-center login-container">
      <div class="card border-0 shadow login-card">
          <div class="card-body p-5">
              <div class="text-center mb-4">
                  <img src="assets/images/logo.svg" alt="L&F logo" class="img-fluid login-logo">
              </div>

              <h2 class="text-center login-title">Register</h2>

              <form @submit.prevent="handleRegister">
                  <div class="form-floating mb-3">
                      <input id="name" class="form-control" v-model="name" required placeholder="Full Name">
                      <label for="name" class="form-label">Full Name</label>
                  </div>

                  <div class="form-floating mb-3">
                      <input id="email" type="email" class="form-control" v-model="email" required placeholder="Email">
                      <label for="email" class="form-label">Email</label>
                  </div>

                  <div class="form-floating mb-3">
                      <input id="phone" type="tel" class="form-control" v-model="phone" required placeholder="Phone">
                      <label for="phone" class="form-label">Phone</label>
                  </div>

                  <div class="form-floating mb-4">
                      <input type="password" id="password" class="form-control" v-model="password" required placeholder="Password">
                      <label for="password" class="form-label">Password</label>
                  </div>

                  <button type="submit" class="btn w-100 py-3 fw-semibold mb-3 login-btn">
                      <span>Register</span>
                  </button>

                  <div v-if="errorMessage" class="alert alert-danger mt-3 mb-0">
                      {{ errorMessage }}
                  </div>

                  <div v-if="successMessage" class="alert alert-success mt-3 mb-0">
                      {{ successMessage }}
                  </div>

                  <p class="text-center mt-3">
                      Already have an account? 
                      <a href="javascript:void(0)" @click="navigateToLogin" class="text-primary">Login here</a>
                  </p>
              </form>
          </div>
      </div>
  </div>
  `,
  data() {
    return {
      name: '',
      email: '',
      phone: '',
      password: '',
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async handleRegister() {
      this.errorMessage = '';
      this.successMessage = '';

      if (this.password.length < 4) {
        this.errorMessage = 'Password must be at least 4 characters long.';
        return;
      }

      try {
        const response = await fetch('http://localhost:9091/api/users', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            name: this.name,
            email: this.email,
            phone: this.phone,
            password: this.password
          })
        });

        if (response.ok) {
          this.successMessage = 'Registration successful! Redirecting to login...';
          setTimeout(() => {
            this.$router.push('/login'); // Redirect to login page
          }, 2000);
        } else {
          this.errorMessage = 'Error registering user. Please try again.';
        }
      } catch (error) {
        this.errorMessage = 'Network error. Please check your connection.';
      }
    },
    navigateToLogin() {
      this.$router.push('/login'); // Ensure Vue Router is being used
    }
  }
};
