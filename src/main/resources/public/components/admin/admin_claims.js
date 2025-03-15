export default {
  template: `
<div class="container mt-5">
    <h1>Claims</h1>
    <ul class="list-group">
        <li v-for="claim in claims" :key="claim.id" class="list-group-item">
            {{ claim.description }} - <strong>{{ claim.status }}</strong>
        </li>
    </ul>
</div>
  `,
  data() {
      return {
          claims: []
      };
  },
  async created() {
      try {
          // Retrieve the JWT token from localStorage
          const token = localStorage.getItem('authToken');

          // Check if the token exists
          if (!token) {
              console.error("No JWT token found in localStorage");
              return;
          }

          // Fetch claims with the Authorization header
          const response = await fetch('/api/claims', {
              method: 'GET',
              headers: {
                  'Authorization': `Bearer ${token}`, // Include the token in the Authorization header
                  'Content-Type': 'application/json'
              }
          });

          // Handle non-2xx responses
          if (!response.ok) {
              throw new Error(`Failed to fetch claims: ${response.statusText}`);
          }

          // Parse and store the claims data
          this.claims = await response.json();
      } catch (error) {
          console.error("Error fetching claims:", error.message);
      }
  }
};