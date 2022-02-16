// Copyright (c) 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#ifndef SOURCE_FUZZ_REPLAYER_H_
#define SOURCE_FUZZ_REPLAYER_H_

#include <memory>
#include <vector>

#include "source/fuzz/protobufs/spirvfuzz_protobufs.h"
#include "spirv-tools/libspirv.hpp"

namespace spvtools {
namespace fuzz {

// Transforms a SPIR-V module into a semantically equivalent SPIR-V module by
// applying a series of pre-defined transformations.
class Replayer {
 public:
  // Possible statuses that can result from running the replayer.
  enum class ReplayerResultStatus {
    kComplete,
    kFailedToCreateSpirvToolsInterface,
    kInitialBinaryInvalid,
    kReplayValidationFailure,
    kTooManyTransformationsRequested,
  };

  struct ReplayerResult {
    ReplayerResultStatus status;
    std::vector<uint32_t> transformed_binary;
    protobufs::TransformationSequence applied_transformations;
  };

  Replayer(spv_target_env target_env, MessageConsumer consumer,
           const std::vector<uint32_t>& binary_in,
           const protobufs::FactSequence& initial_facts,
           const protobufs::TransformationSequence& transformation_sequence_in,
           uint32_t num_transformations_to_apply, uint32_t first_overflow_id,
           bool validate_during_replay,
           spv_validator_options validator_options);

  // Disables copy/move constructor/assignment operations.
  Replayer(const Replayer&) = delete;
  Replayer(Replayer&&) = delete;
  Replayer& operator=(const Replayer&) = delete;
  Replayer& operator=(Replayer&&) = delete;

  ~Replayer();

  // Attempts to apply the first |num_transformations_to_apply_| transformations
  // from |transformation_sequence_in_| to |binary_in_|.  Initial facts about
  // the input binary and the context in which it will execute are provided via
  // |initial_facts_|.
  //
  // |first_overflow_id_| should be set to 0 if overflow ids are not available
  // during replay.  Otherwise |first_overflow_id_| must be larger than any id
  // referred to in |binary_in_| or |transformation_sequence_in_|, and overflow
  // ids will be available during replay starting from this value.
  //
  // On success, returns a successful result status together with the
  // transformations that were successfully applied and the binary resulting
  // from applying them.  Otherwise, returns an appropriate result status
  // together with an empty binary and empty transformation sequence.
  ReplayerResult Run();

 private:
  // Target environment.
  const spv_target_env target_env_;

  // Message consumer.
  MessageConsumer consumer_;

  // The binary to which transformations are to be applied.
  const std::vector<uint32_t>& binary_in_;

  // Initial facts known to hold in advance of applying any transformations.
  const protobufs::FactSequence& initial_facts_;

  // The transformations to be replayed.
  const protobufs::TransformationSequence& transformation_sequence_in_;

  // The number of transformations that should be replayed.
  const uint32_t num_transformations_to_apply_;

  // Zero if overflow ids are not available, otherwise hold the value of the
  // smallest id that may be used for overflow purposes.
  const uint32_t first_overflow_id_;

  // Controls whether the validator should be run after every replay step.
  const bool validate_during_replay_;

  // Options to control validation
  spv_validator_options validator_options_;
};

}  // namespace fuzz
}  // namespace spvtools

#endif  // SOURCE_FUZZ_REPLAYER_H_
