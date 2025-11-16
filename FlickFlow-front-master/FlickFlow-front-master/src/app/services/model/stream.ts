export interface StreamingOption {
  quality: string;
  url: string;
}

export interface Stream {
  streamingOptions: StreamingOption[];
  availability: string;
  geoRestrictions: string[];
}
