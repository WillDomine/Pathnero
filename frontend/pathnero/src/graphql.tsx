import { gql } from '@apollo/client'

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
const DELETE_JOB = gql`
  mutation DeleteJob($id: ID!) {
    deleteJob(id: $id) 
  }
`

export { GET_JOBS, DELETE_JOB }