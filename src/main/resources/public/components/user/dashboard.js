export default {
  template: `
<div class="container mt-5">
    <h2 class="text-center">Your Claims</h2>
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
