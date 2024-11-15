import pandas as pd
import matplotlib.pyplot as plt
import os

results_dir = "sharding_experiment_results"
output_file = f"{results_dir}/expanded_results_plot.png"
csv_file = f"{results_dir}/combined_results_all.csv"
os.makedirs(results_dir, exist_ok=True)

data = pd.read_csv(csv_file)

sharded_data = data[data['collectionName'] == 'listingCollection']['averageLatency'].reset_index(drop=True)
non_sharded_data = data[data['collectionName'] == 'nonShardedCollection']['averageLatency'].reset_index(drop=True)

# Plot line chart for each collection type
plt.figure(figsize=(12, 8))
plt.plot(sharded_data.index, sharded_data, marker='o', label='Sharded Listing Collection')
plt.plot(non_sharded_data.index, non_sharded_data, marker='o', label='Non-sharded Listing Collection')

# Highlight regions for each database size (1k, 10k, 30k, 100k, 250k, 500k, 1M entries)
colors = ['#e0f7fa', '#b2ebf2', '#80deea', '#4dd0e1', '#26c6da', '#00acc1', '#00838f']
database_sizes = ['1k entries', '10k entries', '30k entries', '100k entries', '250k entries', '500k entries', '1M entries']
for i in range(7):
    plt.axvspan(i * 10, (i + 1) * 10 - 1, color=colors[i], alpha=0.3, label=database_sizes[i])

plt.title("Latency Comparison: Sharded vs Non-Sharded Collection (Various Database Sizes)")
plt.xlabel("Query Index")
plt.ylabel("Average Latency (ms)")
plt.legend(title="Collection Type")
plt.xticks(rotation=45)
plt.tight_layout()

# Save plot
plt.savefig(output_file)
print(f"Plot saved to {output_file}")
