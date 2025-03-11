export default {
  template: `
  <div class="container d-flex justify-content-center align-items-center login-container">
      <div class="card border-0 shadow login-card">
          <div class="card-body p-5">
              <div class="text-center mb-4">
                  <img src="assets/images/logo.svg" alt="L&F logo" class="img-fluid login-logo">
              </div>

              <h2 class="text-center login-title">Login</h2>
              
              <form @submit.prevent="handleLogin">
                  <div class="form-floating mb-3">
                      <input type="email" id="email" class="form-control" 
                             v-model="email" required placeholder="Email">
                      <label for="email" class="form-label">Email</label>
                  </div>

                  <div class="form-floating mb-4">
                      <input type="password" id="password" class="form-control" 
                             v-model="password" required placeholder="Password">
                      <label for="password" class="form-label">Password</label>
                  </div>

                  <button type="submit" class="btn w-100 py-3 fw-semibold mb-3 login-btn">
                      <span>Login</span>
                  </button>

                  <div v-if="errorMessage" class="alert alert-danger mt-3 mb-0">
                      {{ errorMessage }}
                  </div>
              </form>
          </div>
      </div>
  </div>
  `,
  data() {
      return {
        email: '',
        password: '',
        errorMessage: ''
      };
    },
    methods: {
      async handleLogin() {
        this.errorMessage = '';

        try {
          const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: this.email, password: this.password })
          });

          if (!response.ok) throw new Error('Invalid email or password');

          const data = await response.json();



          // Check if login is from the Admin or User table
          if (data.role == "ADMIN") {

            this.$root.changeView('admin_dashboard');  // Admin goes to dashboard
          } else {
            this.$root.changeView('user_dashboard');  // User goes to claims page
          }

        } catch (error) {
          this.errorMessage = error.message;
        }
      }
    }
  };