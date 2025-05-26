import { gql, useQuery } from '@apollo/client'
import React from 'react'
import './App.css'

// GraphQL query
const GET_JOBS = gql`
  query GetJobs {
    jobs {
      id
      title
      description
      salary
      company {
        name
      }
    }
  }
`

// TypeScript types for data
interface Company {
  name: string
}
interface Job {
  id: string
  title: string
  company: Company
  description: string
  salary: number
}

interface JobsData {
  jobs: Job[]
}

// Convert regular number into currency format
function formatCurrency(amount: number) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount)
}

function App() {
  const { loading, error, data: job_data } = useQuery<JobsData>(GET_JOBS)
  const [selectedJob, setSelectedJob] = React.useState<Job | null>(null)

  const handleJobClick = (job: Job) => {
    setSelectedJob(job)
  }

  return (
    <div className="container">
      <h1>Pathnero</h1>

      {loading && <p>Loading jobs...</p>}
      {error && <p>Error: {error.message}</p>}

      {job_data && (
        <div className="job-container">
          {/* Left Column: Job List */}
          <div className="job-list">
            {job_data.jobs.map((job) => (
              <div
                className="job-item"
                key={job.id}
                onClick={() => handleJobClick(job)}
              >
                <strong>{job.title}</strong>
                <br />
                <span>at {job.company.name}</span>
              </div>
            ))}
          </div>

          {/* Right Column: Job Details */}
          <div className="job-details">
            {selectedJob ? (
              <>
                <h2>{selectedJob.title}</h2>
                <p><strong>Company:</strong> {selectedJob.company.name}</p>
                <p><strong>Salary:</strong> {formatCurrency(selectedJob.salary)}</p>
                <p><strong>Description:</strong> {selectedJob.description}</p>
                {/* Add more detailed information about the job here */}
              </>
            ) : (
              <p>Select a job to see the details</p>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default App
