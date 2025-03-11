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
      const response = await fetch('/api/claims');
      this.claims = await response.json();
    }
  };
  