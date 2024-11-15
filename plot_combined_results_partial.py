import pandas as pd
import matplotlib.pyplot as plt
import os

results_dir = "sharding_experiment_results"
output_file = f"{results_dir}/combined_results_partial_plot.png"
csv_file = f"{results_dir}/combined_results_partial.csv"
os.makedirs(results_dir, exist_ok=True)

data = pd.read_csv(csv_file)

sharded_data = data[data['collectionName'] == 'listingCollection']['averageLatency'].reset_index(drop=True)[:30]
non_sharded_data = data[data['collectionName'] == 'nonShardedCollection']['averageLatency'].reset_index(drop=True)[:30]

# Plot line chart for each collection type
plt.figure(figsize=(12, 8))
plt.plot(sharded_data.index, sharded_data, marker='o', label='Sharded Listing Collection')
plt.plot(non_sharded_data.index, non_sharded_data, marker='o', label='Non-sharded Listing Collection')

# Highlight regions for each of the selected database sizes (1k, 10k, 30k entries)
colors = ['#e0f7fa', '#b2ebf2', '#80deea']
database_sizes = ['1k entries', '10k entries', '30k entries']
for i in range(3):
    plt.axvspan(i * 10, (i + 1) * 10 - 1, color=colors[i], alpha=0.3, label=database_sizes[i])

plt.title("Latency Comparison: Sharded vs Non-Sharded Collection (1k, 10k, 30k Entries)")
plt.xlabel("Query Index")
plt.ylabel("Average Latency (ms)")
plt.legend(title="Collection Type")
plt.xticks(rotation=45)
plt.tight_layout()

# Save plot
plt.savefig(output_file)
print(f"Plot saved to {output_file}")
