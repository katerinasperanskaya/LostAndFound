export default {
  template: `
<div class="container mt-5">
    <h1>Admin Dashboard</h1>
    <canvas id="statusChart"></canvas>
</div>
  `,
  data() {
    return {
      statusData: { CLAIMED: 0, UNCLAIMED: 0 },
      chartInstance: null, // Store chart instance
    };
  },
  mounted() {
    this.fetchStatusData();
  },
  methods: {
    async fetchStatusData() {
      try {
        const unclaimedRes = await fetch("http://localhost:9091/api/found-items/status/UNCLAIMED");
        const claimedRes = await fetch("http://localhost:9091/api/found-items/status/CLAIMED");

        const unclaimedData = await unclaimedRes.json();
        const claimedData = await claimedRes.json();

        console.log("Unclaimed Data:", unclaimedData);
        console.log("Claimed Data:", claimedData);

        // Ensure only whole numbers
        this.statusData.UNCLAIMED = Math.floor(unclaimedData.length);
        this.statusData.CLAIMED = Math.floor(claimedData.length);

        this.renderChart();
      } catch (error) {
        console.error("Error fetching status data:", error);
      }
    },
    renderChart() {
      const ctx = document.getElementById("statusChart").getContext("2d");

      // Destroy existing chart instance if it exists to avoid duplicate charts
      if (this.chartInstance) {
        this.chartInstance.destroy();
      }

      // new chart
      this.chartInstance = new Chart(ctx, {
        type: "bar",
        data: {
          labels: ["Unclaimed", "Claimed"],
          datasets: [
            {
              label: "Found Items Status",
              data: [this.statusData.UNCLAIMED, this.statusData.CLAIMED],
              backgroundColor: ["#A379C9", "#9AD5CA"]
            }
          ]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1, // only whole numbers
                callback: function (value) {
                  return Number.isInteger(value) ? value : null;
                }
              }
            }
          }
        }
      });
    }
  }
};
